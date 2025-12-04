package dev.rdh.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

/// Single-file JSON5-"compliant" serialization and deserialization.
///
/// @param indentSize the number of spaces to use for indentation. If 0, no indentation is used, and the output is compacted.
/// @param quoteKeys whether to always quote keys in objects. If false, keys that are valid ECMAScript _IdentifierName_s will not be quoted.
/// @param quote the character to use for quoting keys and string values. Must be either `'` or `"`.
/// @param warnUnescapedLineSeparators whether to print a warning to stderr about unescaped `\u2028` and `\u2029` line separators in string literals.
/// @author rdh
/// @see <a href="https://json5.org/">JSON5 specification</a>
/// @see <a href="https://github.com/Nolij/ZSON">ZSON (origin of much of the parsing code)</a>
/// @see <a href="https://gist.github.com/rhysdh540/cc05fd1aa86528c5979685259bcb1b4c">Gist link</a>
public record Json(
		int indentSize,
		boolean quoteKeys,
		char quote,
		boolean warnUnescapedLineSeparators,
		boolean jsonCompliant
) {
	public Json {
		if(indentSize < 0) {
			throw new IllegalArgumentException("Indent size must be non-negative, got: " + indentSize);
		}

		if(quote != '\'' && quote != '"') {
			throw new IllegalArgumentException("quote must be either '\\'' or '\"', got: " + quote);
		}

		if (jsonCompliant) {
			if(!quoteKeys) {
				throw new IllegalArgumentException("JSON-compliant mode requires quoteKeys to be true");
			}

			if(quote != '"') {
				throw new IllegalArgumentException("JSON-compliant mode requires quote to be '\"'");
			}
		}
	}

	/// Default to a compact, standard JSON-compliant format, with quoted keys.
	public Json() {
		this(0, true, '"', true, true);
	}

	public Json(int indentSize) {
		this(indentSize, true, '"', true, true);
	}

	public Json(int indentSize, boolean quoteKeys, char quote) {
		this(indentSize, quoteKeys, quote, true, false);
	}

	// region Helpers

	public static final List<Byte> PRINTABLE_CHARACTER_TYPES = List.of(
			Character.UPPERCASE_LETTER, Character.LOWERCASE_LETTER, Character.TITLECASE_LETTER,
			Character.MODIFIER_LETTER, Character.OTHER_LETTER, Character.LETTER_NUMBER,
			Character.DECIMAL_DIGIT_NUMBER, Character.CONNECTOR_PUNCTUATION, Character.DASH_PUNCTUATION,
			Character.START_PUNCTUATION, Character.END_PUNCTUATION, Character.OTHER_PUNCTUATION,
			Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, Character.PARAGRAPH_SEPARATOR
	);

	private static final HexFormat hex = HexFormat.of().withLowerCase();
	private static final DecimalFormat df = new DecimalFormat(); // no exponent
	private static final DecimalFormat dfe = new DecimalFormat("0.#E0"); // with exponent
	static {
		df.setGroupingUsed(false);
		df.setDecimalSeparatorAlwaysShown(false);
		df.setMaximumFractionDigits(340);
		df.setMinimumFractionDigits(0);

		dfe.setGroupingUsed(false);
		dfe.setDecimalSeparatorAlwaysShown(false);
		dfe.setMaximumFractionDigits(340);
		dfe.setMinimumFractionDigits(0);
	}

	/// Escapes a string by replacing special characters with escape sequences.
	/// The following characters are escaped:
	///
	///  - Control characters: `\0`, `\t`, `\b`, `\n`, `\r`, `\f`
	///  - Quotes: `\'` or `\"`
	///  - Backslash: `\\`
	///  - Non-printable characters, whose types are not in [#PRINTABLE_CHARACTER_TYPES].
	///
	/// @param input the string to escape. May be null.
	/// @return the escaped string, or null if the input was null.
	public String escape(String input) {
		if (input == null || input.isEmpty()) return input;
		StringBuilder sb = new StringBuilder(input.length());

		int c;
		for (int i = 0; i < input.length(); i += Character.charCount(c)) {
			c = input.codePointAt(i);

			switch (c) {
				case '\0' -> sb.append("\\0");
				case '\t' -> sb.append("\\t");
				case '\b' -> sb.append("\\b");
				case '\n' -> sb.append("\\n");
				case '\r' -> sb.append("\\r");
				case '\f' -> sb.append("\\f");
				case '\\' -> sb.append("\\\\");
				case '\'', '"' -> {
					if (c == quote) sb.append('\\');
					sb.appendCodePoint(c);
				}
				default -> {
					int type = Character.getType(c);
					if (PRINTABLE_CHARACTER_TYPES.contains((byte) type)) {
						sb.appendCodePoint(c);
					} else if (c <= 0xFF && !jsonCompliant) {
						sb.append("\\x").append(hex.toHexDigits(c, 2));
					} else if (c <= 0xFFFF) {
						sb.append("\\u").append(hex.toHexDigits(c, 4));
					} else {
						char[] pair = Character.toChars(c);
						for (char ch : pair) {
							sb.append("\\u").append(hex.toHexDigits(ch));
						}
					}
				}
			}
		}

		return sb.toString();
	}

	/// "Un-escapes" a string by replacing escape sequences with their actual characters.
	///
	/// @param input the string to un-escape. May be null.
	/// @return the un-escaped string, or null if the input was null.
	public static String unescape(String input) {
		if (input == null || input.isEmpty()) return input;
		StringBuilder sb = new StringBuilder(input.length() + 10);
		char[] chars = input.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			if (ch != '\\') {
				sb.append(ch);
				continue;
			}

			if (i + 1 >= chars.length) {
				throw new IllegalArgumentException("Invalid escape: trailing backslash");
			}

			char next = chars[++i];
			switch (next) {
				case 'b' -> sb.append('\b');
				case 'f' -> sb.append('\f');
				case 'n' -> sb.append('\n');
				case 'r' -> sb.append('\r');
				case 't' -> sb.append('\t');
				case '0' -> sb.append('\0');
				case '\'', '"', '\\' -> sb.append(next);
				case 'x' -> {
					if (i + 2 >= chars.length) {
						throw new IllegalArgumentException("Incomplete \\x escape");
					}
					String hex = new String(new char[]{chars[++i], chars[++i]});
					sb.appendCodePoint(Integer.parseInt(hex, 16));
				}
				case 'u' -> {
					if (i + 4 >= chars.length) {
						throw new IllegalArgumentException("Incomplete \\u escape");
					}
					String hex = new String(chars, i + 1, 4);
					i += 4;
					sb.appendCodePoint(Integer.parseInt(hex, 16));
				}

				// JSON5 spec says to ignore invalid escape sequences
				default -> sb.append(next);
			}
		}
		return sb.toString();
	}

	private static final List<Byte> IDENFIFIER_START_TYPES = Arrays.asList(
			Character.UPPERCASE_LETTER, Character.LOWERCASE_LETTER, Character.TITLECASE_LETTER,
			Character.MODIFIER_LETTER, Character.OTHER_LETTER, Character.LETTER_NUMBER
	);

	private static final List<Byte> IDENFIFIER_TYPES = Arrays.asList(
			Character.UPPERCASE_LETTER, Character.LOWERCASE_LETTER, Character.TITLECASE_LETTER,
			Character.MODIFIER_LETTER, Character.OTHER_LETTER, Character.LETTER_NUMBER,

			Character.NON_SPACING_MARK, Character.COMBINING_SPACING_MARK,
			Character.DECIMAL_DIGIT_NUMBER, Character.CONNECTOR_PUNCTUATION
	);

	/// Checks if the given character is a valid EMCAScript *IdentifierName* character.
	///
	/// @param c the code point to check
	/// @return `true` if the character is a valid identifier character, `false` otherwise
	/// @see <a href="https://262.ecma-international.org/5.1/#sec-7.6">ECMAScript 5.1 &sect;7.6</a>
	public static boolean isIdentifierChar(int c) {
		return c == '_' || c == '$' || IDENFIFIER_TYPES.contains((byte) Character.getType(c));
	}

	/// Checks if the given character is a valid ECMAScript *IdentifierStart* character.
	///
	/// @param c the code point to check
	/// @return `true` if the character is a valid identifier start character, `false` otherwise
	/// @see <a href="https://262.ecma-international.org/5.1/#sec-7.6">ECMAScript 5.1 &sect;7.6</a>
	public static boolean isIdentifierStart(int c) {
		return c == '_' || c == '$' || IDENFIFIER_START_TYPES.contains((byte) Character.getType(c));
	}

	/// Checks if the given string is a valid ECMAScript _IdentifierName_.
	///
	/// @param name the string to check
	/// @return `true` if the string is a valid identifier, `false` otherwise
	/// @see <a href="https://262.ecma-international.org/5.1/#sec-7.6">ECMAScript 5.1 &sect;7.6</a>
	public static boolean isValidIdentifierName(String name) {
		if(name == null || name.isEmpty()) return false;

		int firstChar = name.codePointAt(0);
		if(!isIdentifierStart(firstChar)) return false;

		for(int i = Character.charCount(firstChar); i < name.length(); ) {
			int codePoint = name.codePointAt(i);
			if(!isIdentifierChar(codePoint)) return false;
			i += Character.charCount(codePoint);
		}

		return true;
	}

	/// Creates a JSON object (as a [Map]) from the given entries.
	/// The concrete type of the Map is unlikely to change but shouldn't be depended on in any case.
	///
	/// @param entries the entries to include in the object. Each entry should have a String key and a value of any type.
	/// @return a Map representing the JSON object.
	@SafeVarargs
	public static Map<String, ?> object(Entry<String, ?>... entries) {
		Map<String, Object> map = new LinkedHashMap<>();
		for(Entry<String, ?> entry : entries) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	/// Helper for [#object(Entry...)] to create a Map.Entry, with a nullable value.
	///
	/// @param key   the key for the entry, must not be null.
	/// @param value the value for the entry, may be null.
	/// @return a Map.Entry with the given key and value.
	public static Entry<String, ?> e(String key, Object value) {
		return new SimpleEntry<>(key, value);
	}

	/// Creates a JSON array (as a [List]) from the given values.
	/// The concrete type of the List is unlikely to change but shouldn't be depended on in any case.
	///
	/// @param values the values to include in the array. Each value can be of any type.
	/// @return a List representing the JSON array.
	public static List<?> array(Object... values) {
		return new ArrayList<>(Arrays.asList(values));
	}
	// endregion

	// region Writer
	public String toString(Object data) {
		StringBuilder sb = new StringBuilder();
		try {
			write(data, sb);
		} catch(IOException e) {
			throw new RuntimeException("Failed to write JSON", e);
		}
		return sb.toString();
	}

	public void write(Object data, Appendable out) throws IOException {
		// use object identity to detect circular references
		Set<Object> seen = Collections.newSetFromMap(new IdentityHashMap<>());
		writeValue(data, out, 0, seen);
	}

	private void writeMap(Map<String, ?> map, Appendable out, int indentDepth, Set<Object> seen) throws IOException {
		if(seen.contains(map)) {
			throw new IllegalArgumentException("Circular reference detected in Map");
		}
		seen.add(map);

		var entries = map.entrySet();

		if(entries.isEmpty()) {
			out.append("{}");
			seen.remove(map);
			return;
		}

		out.append('{');
		boolean pretty = indentSize > 0;
		if(pretty) {
			out.append('\n');
		}

		Set<String> seenKeys = new HashSet<>();
		for(var it = entries.iterator(); it.hasNext(); ) {
			Entry<String, ?> entry = it.next();
			String key = entry.getKey();
			if(!seenKeys.add(key)) {
				throw new IllegalArgumentException("Duplicate key found in Map: " + key);
			}

			Object value = entry.getValue();

			writeIndent(out, indentDepth + 1);

			out.append(quoteIfNeeded(key));

			out.append(':');
			if(pretty) {
				out.append(' ');
			}

			// caller handles indentation
			writeValue(value, out, indentDepth + 1, seen);

			if(it.hasNext()) {
				out.append(',');
			}

			if(pretty) {
				out.append('\n');
			}
		}

		writeIndent(out, indentDepth);
		out.append('}');
		seen.remove(map);
	}

	private void writeArray(Iterable<?> array, Appendable out, int indentDepth, Set<Object> seen) throws IOException {
		if(seen.contains(array)) {
			throw new IllegalArgumentException("Circular reference detected in Array/Iterable");
		}
		seen.add(array);

		Iterator<?> it = array.iterator();
		if(!it.hasNext()) {
			out.append("[]");
			seen.remove(array);
			return;
		}

		out.append('[');
		boolean pretty = indentSize > 0;

		// write newlines if any element is a "complex" type (object or array)
		boolean writeNewlines = false;
		while(it.hasNext()) {
			Object o = it.next();
			if(o instanceof Map<?, ?> || o instanceof Iterable<?> || o != null && o.getClass().isArray()) {
				writeNewlines = true;
				break;
			}
		}

		writeNewlines &= pretty;

		if(writeNewlines) {
			out.append('\n');
		}

		it = array.iterator();
		while(it.hasNext()) {
			Object element = it.next();

			if(writeNewlines) {
				writeIndent(out, indentDepth + 1);
			}

			writeValue(element, out, indentDepth + 1, seen);

			if(it.hasNext()) {
				out.append(',');
			}

			if(writeNewlines) {
				out.append('\n');
			} else if(pretty && it.hasNext()) {
				out.append(' ');
			}
		}

		if(writeNewlines) {
			writeIndent(out, indentDepth);
		}
		out.append(']');
		seen.remove(array);
	}

	private void writeValue(Object value, Appendable out, int indentDepth, Set<Object> seen) throws IOException {
		switch(value) {
			case null -> out.append("null");
			case String s -> out.append(quote).append(escape(s)).append(quote);
			case Boolean b -> out.append(String.valueOf(b));
			case Number n -> {
				BigDecimal bd;
				if (value instanceof BigDecimal) {
					bd = (BigDecimal) n;
				} else if (value instanceof BigInteger) {
					bd = new BigDecimal((BigInteger) n);
				} else {
					bd = BigDecimal.valueOf(n.doubleValue());
				}

				bd = bd.stripTrailingZeros();

				if (bd.abs().compareTo(BigDecimal.valueOf(1_000_000)) < 0) {
					out.append(df.format(bd));
				} else {
					out.append(dfe.format(bd));
				}
			}
			case Map<?, ?> m -> {
				@SuppressWarnings("unchecked")
				Map<String, ?> map = (Map<String, ?>) m;
				writeMap(map, out, indentDepth, seen);
			}
			case Iterable<?> arr -> writeArray(arr, out, indentDepth, seen);
			case Object arr when arr.getClass().isArray() -> {
				int len = Array.getLength(arr);
				List<Object> list = new ArrayList<>(len);
				for(int i = 0; i < len; i++) {
					list.add(Array.get(arr, i));
				}
				writeArray(list, out, indentDepth, seen);
			}
			default -> throw new IllegalArgumentException("Unrecognized JSON type: " + value.getClass().getName());
		}
	}

	private String quoteIfNeeded(String key) {
		if(!quoteKeys && isValidIdentifierName(key)) {
			return key;
		} else {
			return quote + escape(key) + quote;
		}
	}

	private void writeIndent(Appendable out, int indentDepth) throws IOException {
		// if indentSize is 0, this will do nothing
		for(int i = 0; i < indentDepth * indentSize; i++) {
			out.append(' ');
		}
	}

	// endregion

	// region Parser
	// Note: these are instance methods but don't use the instance state.

	public <T> T parse(String input) throws IOException {
		if(input == null || input.isEmpty()) {
			return null;
		}
//		return parse(Reader.of(input));
		return parse(new java.io.StringReader(input));
	}

	@SuppressWarnings("unchecked")
	public <T> T parse(Reader input) throws IOException {
		if(!input.markSupported()) {
			input = new BufferedReader(input);
		}

		while(skipComment(input) || skipWhitespace(input));

		int c = input.read();
		return (T) switch(c) {
			case -1 -> throw unexpectedEOF();
			case '{' -> parseObject(input);
			case '[' -> parseArray(input);
			case '"', '\'' -> unescape(parseString(input, c));
			case '.', '-', '+', 'N', 'I',
				 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> parseNumber(input, c);
			case 't' -> {
				expectString(input, c, "rue");
				yield Boolean.TRUE;
			}
			case 'f' -> {
				expectString(input, c, "alse");
				yield Boolean.FALSE;
			}
			case 'n' -> {
				expectString(input, c, "ull");
				yield null;
			}
			default -> throw unexpected(c);
		};
	}

	private Map<String, ?> parseObject(Reader input) throws IOException {
		Map<String, Object> map = new LinkedHashMap<>();

		boolean comma = false;
		boolean colon = false;
		String key = null;

		while(true) {
			if(skipWhitespace(input) || skipComment(input))
				continue;

			input.mark(1);
			int c = input.read();

			if(c == '}')
				return map;

			if(comma) {
				if(c != ',')
					throw new IllegalArgumentException("Expected comma, got " + (char) c);

				comma = false;
				continue;
			}

			if(colon) {
				if(c != ':')
					throw new IllegalArgumentException("Expected colon, got " + (char) c);

				colon = false;
				continue;
			}

			if(c == -1)
				throw unexpectedEOF();

			if(key == null) {
				key = switch(c) {
					case '"', '\'' -> unescape(parseString(input, c));
					default -> {
						if(isIdentifierStart(c) || c == '\\') {
							yield parseIdentifier(input, c);
						} else {
							throw unexpected(c);
						}
					}
				};
				colon = true;
			} else {
				input.reset();
				Object value = parse(input);
				map.put(key, value);
				key = null;
				comma = true;
			}
		}
	}

	private List<?> parseArray(Reader input) throws IOException {
		List<Object> list = new ArrayList<>();
		boolean comma = false;

		while(true) {
			if(skipWhitespace(input) || skipComment(input))
				continue;

			input.mark(1);
			int c = input.read();
			if(c == ']')
				return list;

			if(comma) {
				if(c != ',')
					throw new IllegalArgumentException("Expected comma, got " + (char) c);

				comma = false;
				continue;
			}

			if(c == -1)
				throw unexpectedEOF();

			input.reset();
			Object value = parse(input);
			list.add(value);
			comma = true;
		}
	}

	private String parseString(Reader input, int firstChar) throws IOException {
		int escapes = 0;
		StringBuilder output = new StringBuilder();
		int c;

		while((c = input.read()) != -1) {
			if(c == firstChar) {
				if(escapes == 0)
					return output.toString();

				output.append(Character.toChars(c));
				escapes--;
			} else if(isLineTerminator(c)) {
				if(escapes == 0) {
					if(c == '\u2028' || c == '\u2029') {
						System.err.println("[JSON] Warning: unescaped line separator in string literal");
					} else {
						throw new IllegalArgumentException("Unexpected newline");
					}
				}

				escapes = 0;
			} else if(c == '\\') {
				escapes++;
				if(escapes == 2) {
					output.append("\\\\");
					escapes = 0;
				}
			} else {
				if(escapes == 1) {
					output.append('\\');
				}
				output.append(Character.toChars(c));
				escapes = 0;
			}
		}
		throw unexpectedEOF();
	}

	private String parseIdentifier(Reader input, int firstChar) throws IOException {
		StringBuilder output = new StringBuilder();
		boolean escaped = firstChar == '\\';
		if(!escaped) {
			output.appendCodePoint(firstChar);
		}

		int c;
		input.mark(1);
		while((c = input.read()) != -1) {
			if(escaped) {
				if(c == 'n' || c == 'r') {
					throw unexpected(c);
				}
				output.append(unescape("\\" + (char) c));
				input.mark(1);
				escaped = false;
			} else if(c == '\\') {
				input.mark(1);
				escaped = true;
			} else if(isIdentifierChar(c)) {
				input.mark(1);
				output.append(Character.toChars(c));
			} else {
				input.reset();
				return output.toString();
			}
		}

		throw unexpectedEOF();
	}

	/**
	 * @implNote limited to doubles for now, subject to change in the future.
	 */
	private Number parseNumber(Reader input, int firstChar) throws IOException {
		switch(firstChar) {
			case '+' -> {
				return parseNumber(input, input.read());
			}
			case '-' -> {
				return -(Double) parseNumber(input, input.read());
			}
			case 'N' -> {
				expectString(input, firstChar, "aN");
				return Double.NaN;
			}
			case 'I' -> {
				expectString(input, firstChar, "nfinity");
				return Double.POSITIVE_INFINITY;
			}
			case '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' -> {
				return parseDecimal(input, firstChar);
			}
			case '0' -> {
				input.mark(1);
				int c = input.read();
				if(c != 'x' && c != 'X') {
					input.reset();
					return parseDecimal(input, firstChar);
				}

				// Hexadecimal number
				StringBuilder value = new StringBuilder();
				input.mark(1);
				while((c = input.read()) != -1) {
					if(c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F') {
						input.mark(1);
						value.appendCodePoint(c);
					} else {
						input.reset();
						return (double) Long.parseLong(value.toString(), 16);
					}
				}

				throw unexpectedEOF();
			}
			default -> throw unexpected(firstChar);
		}
	}

	private double parseDecimal(Reader input, int firstChar) throws IOException {
		StringBuilder value = new StringBuilder();
		value.appendCodePoint(firstChar);

		boolean hasDecimal = false;
		boolean hasExponent = false;

		input.mark(1);
		int c;
		while((c = input.read()) != -1) {
			if(Character.isDigit(c)) {
				value.appendCodePoint(c);
				input.mark(1);
			} else if(c == '.' && !hasDecimal && !hasExponent) {
				hasDecimal = true;
				value.appendCodePoint(c);
				input.mark(1);
			} else if((c == 'e' || c == 'E') && !hasExponent) {
				hasExponent = true;
				value.appendCodePoint(c);
				input.mark(1);
				c = input.read();
				if(c == '+' || c == '-') {
					value.appendCodePoint(c);
				} else {
					input.reset();
				}
			} else {
				input.reset();
				break;
			}
		}

		return Double.parseDouble(value.toString());
	}

	private boolean skipWhitespace(Reader input) throws IOException {
		input.mark(1);
		int c;
		int skipped = 0;
		while((c = input.read()) != -1) {
			if(!isWhitespace(c) && !isLineTerminator(c)) {
				input.reset();

				return skipped != 0;
			}

			skipped++;
			input.mark(1);
		}

		throw unexpectedEOF();
	}

	private boolean skipComment(Reader input) throws IOException {
		input.mark(2);
		int c = input.read();
		if(c != '/') {
			input.reset();
			return false;
		}

		c = input.read();
		if(c == '/') {
			while((c = input.read()) != -1 && !isLineTerminator(c)) ;

			return true;
		} else if(c == '*') {
			while((c = input.read()) != -1)
				if(c == '*' && input.read() == '/')
					return true;

			throw unexpectedEOF();
		} else {
			input.reset();
		}

		return false;
	}

	// https://262.ecma-international.org/5.1/#sec-7.3
	private static boolean isLineTerminator(int c) {
		return c == '\n' || c == '\r' || c == '\u2028' || c == '\u2029';
	}

	// https://spec.json5.org/#white-space
	private static boolean isWhitespace(int c) {
		return c == '\t' || c == '\n' || c == '\u000B' || c == '\u000C' || c == '\r'
				|| c == ' ' || c == '\u00A0' || c == '\u2028' || c == '\u2029' || c == '\uFEFF'
				|| Character.getType(c) == Character.SPACE_SEPARATOR;
	}

	private static void expectString(Reader input, int firstChar, String expected) throws IOException {
		char[] chars = new char[expected.length()];
		int read = input.read(chars);
		String str = new String(chars);
		if(read != expected.length() || !str.equals(expected)) {
			throw new IllegalArgumentException("Expected '" + (char) firstChar + expected + "', got '" + (char) firstChar + str + "'");
		}
	}

	private static IllegalArgumentException unexpected(int ch) {
		return new IllegalArgumentException("Unexpected character: " + (char) ch);
	}

	private static IllegalArgumentException unexpectedEOF() {
		return new IllegalArgumentException("Unexpected EOF");
	}

	// endregion
}
