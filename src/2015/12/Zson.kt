import java.io.BufferedReader
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.Double
import java.lang.Long
import java.math.BigInteger
import java.util.*

object Zson {
    fun <T> parse(input: String): T? {
        return parse(BufferedReader(StringReader(input)))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> parse(input: Reader): T? {
        var input = input
        if (!input.markSupported()) {
            input = BufferedReader(input)
        }

        while (true) {
            if (skipWhitespace(input) || skipComment(input)) continue

            val ch = input.read()
            if (ch == -1) {
                throw unexpectedEOF()
            }

            when (ch) {
                '{'.code -> {
                    return parseObject(input) as T?
                }

                '['.code -> {
                    return parseArray(input) as T?
                }

                '"'.code, '\''.code -> {
                    return unescape(parseString(input, ch.toChar())) as T?
                }

                '.'.code, '-'.code, '+'.code, 'N'.code, 'I'.code,
                '0'.code, '1'.code, '2'.code, '3'.code, '4'.code,
                '5'.code, '6'.code, '7'.code, '8'.code, '9'.code -> {
                    return parseNumber(input, ch.toChar()) as T?
                }

                'n'.code -> {
                    val chars = CharArray(3)
                    if (input.read(chars) == 3 && chars[0] == 'u' && chars[1] == 'l' && chars[2] == 'l') {
                        return null
                    } else {
                        throw IllegalArgumentException("Expected 'null', got 'n" + String(chars) + "'")
                    }
                }

                't'.code, 'f'.code -> {
                    return parseBoolean(input, ch.toChar()) as T
                }
            }

            throw unexpected(ch)
        }
    }

    private fun parseObject(input: Reader): MutableMap<String?, Any?> {
        val map: MutableMap<String?, Any?> = LinkedHashMap<String?, Any?>()

        var comma = false
        var colon = false
        var key: String? = null

        while (true) {
            if (skipWhitespace(input) || skipComment(input)) continue

            input.mark(1)
            val ch = input.read()

            if (ch == '}'.code) return map

            if (comma) {
                require(ch == ','.code) { "Expected comma, got " + ch.toChar() }

                comma = false
                continue
            }

            if (colon) {
                require(ch == ':'.code) { "Expected colon, got " + ch.toChar() }

                colon = false
                continue
            }

            if (ch == -1) throw unexpectedEOF()

            if (key == null) {
                key = when (ch) {
                    '"'.code, '\''.code -> unescape(parseString(input, ch.toChar()))
                    else -> {
                        if (Character.isJavaIdentifierStart(ch) || ch == '\\'.code) {
                            parseIdentifier(input, ch)
                        } else {
                            throw unexpected(ch)
                        }
                    }
                }
                colon = true
            } else {
                input.reset()
                val value = parse<Any?>(input)
                map.put(key, value)
                key = null
                comma = true
            }
        }
    }

    private fun parseArray(input: Reader): MutableList<Any?> {
        val list = ArrayList<Any?>()
        var comma = false

        while (true) {
            try {
                if (skipWhitespace(input) || skipComment(input)) continue

                input.mark(1)
                val ch = input.read()
                if (ch == ']'.code) return list

                if (comma) {
                    require(ch == ','.code) { "Expected comma, got " + ch.toChar() }

                    comma = false
                    continue
                }

                if (ch == -1) throw unexpectedEOF()

                input.reset()
                val value = parse<Any?>(input)
                list.add(value)
                comma = true
            } catch (e: IOException) {
                throw IllegalArgumentException(e)
            }
        }
    }

    private fun parseString(input: Reader, start: Char): String {
        var escapes = 0
        val output = StringBuilder()
        var c: Int

        while ((input.read().also { c = it }) != -1) {
            if (c == start.code) {
                if (escapes == 0) return output.toString()

                output.append(Character.toChars(c))
                escapes--
            }

            if (isLineTerminator(c)) {
                if (escapes == 0) {
                    if (c == '\u2028'.code || c == '\u2029'.code) {
                        System.err.println("[ZSON] Warning: unescaped line separator in string literal")
                    } else {
                        throw IllegalArgumentException("Unexpected newline")
                    }
                }

                escapes = 0
                continue
            }

            if (c == '\\'.code) {
                escapes++
                if (escapes == 2) {
                    output.append("\\\\")
                    escapes = 0
                }
            } else {
                if (escapes == 1) {
                    output.append('\\')
                }
                output.append(Character.toChars(c))
                escapes = 0
            }
        }
        throw unexpectedEOF()
    }

    private fun parseIdentifier(input: Reader, start: Int): String {
        val output = StringBuilder()
        var escaped = start == '\\'.code

        if (!escaped) output.append(start.toChar())

        var c: Int
        input.mark(1)
        while ((input.read().also { c = it }) != -1) {
            if (escaped) {
                if (c == 'n'.code || c == 'r'.code) {
                    throw unexpected(c)
                }
                output.append(unescape("\\" + c.toChar()))
                input.mark(1)
                escaped = false
            } else if (c == '\\'.code) {
                input.mark(1)
                escaped = true
            } else if (isIdentifierChar(c)) {
                input.mark(1)
                output.append(Character.toChars(c))
            } else {
                input.reset()
                return output.toString()
            }
        }

        throw unexpectedEOF()
    }

    private fun isIdentifierChar(c: Int): Boolean {
        if (c == '_'.code || c == '$'.code) return true
        val type = Character.getType(c)
        return type == Character.UPPERCASE_LETTER.toInt() || type == Character.LOWERCASE_LETTER.toInt() || type == Character.TITLECASE_LETTER.toInt() ||
                type == Character.MODIFIER_LETTER.toInt() || type == Character.OTHER_LETTER.toInt() || type == Character.LETTER_NUMBER.toInt() ||
                type == Character.NON_SPACING_MARK.toInt() || type == Character.COMBINING_SPACING_MARK.toInt() || type == Character.DECIMAL_DIGIT_NUMBER.toInt() ||
                type == Character.CONNECTOR_PUNCTUATION.toInt()
    }

    private fun isWhitespace(c: Int): Boolean {
        return c == '\t'.code || c == '\n'.code || c == '\u000c'.code || c == '\r'.code || c == ' '.code
                || c == 0x00A0 || c == 0xFEFF || Character.getType(c) == Character.SPACE_SEPARATOR.toInt()
    }

    private fun isLineTerminator(c: Int): Boolean {
        return c == '\n'.code || c == '\r'.code || c == '\u2028'.code || c == '\u2029'.code
    }

    private fun parseBoolean(input: Reader, start: Char): Boolean {
        return if (start == 't') {
            val chars = CharArray(3)
            if (input.read(chars) == 3 && chars[0] == 'r' && chars[1] == 'u' && chars[2] == 'e') {
                true
            } else {
                throw IllegalArgumentException("Expected 'true', got 't" + String(chars) + "'")
            }
        } else {
            val chars = CharArray(4)
            if (input.read(chars) == 4 && chars[0] == 'a' && chars[1] == 'l' && chars[2] == 's' && chars[3] == 'e') {
                false
            } else {
                throw IllegalArgumentException("Expected 'false', got 'f" + String(chars) + "'")
            }
        }
    }

    private fun parseNumber(input: Reader, start: Char): Number? {
        when (start) {
            '-' -> {
                val numberValue = parseNumber(input, input.read().toChar())
                return when (numberValue) {
                    is Double -> -numberValue.toDouble()
                    is Long -> -numberValue.toLong()
                    is BigInteger -> numberValue.negate()
                    else -> -(numberValue as Int)
                }
            }

            'N' -> {
                var n = input.read()
                if (n != 'a'.code || (input.read().also { n = it }) != 'N'.code) {
                    throw unexpected(n)
                }

                return Double.NaN
            }

            'I' -> {
                val chars = CharArray(7)
                if ((input.read(chars) != 7)) throw unexpectedEOF()
                require("nfinity" == String(chars)) { "Expected 'Infinity', got 'I" + String(chars) + "'" }

                return Double.POSITIVE_INFINITY
            }

            '+', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                return parseDecimal(input, start)
            }

            '0' -> {
                input.mark(1)
                var c = input.read()
                if (c == 'x'.code || c == 'X'.code) {
                    val hexValueBuilder = StringBuilder()
                    input.mark(1)
                    while ((input.read().also { c = it }) != -1) {
                        if (Character.isDigit(c) || (c >= 'a'.code && c <= 'f'.code) || (c >= 'A'.code && c <= 'F'.code)) {
                            input.mark(1)
                            hexValueBuilder.append(Character.toChars(c))
                        } else {
                            input.reset()
                            val l = Long.parseLong(hexValueBuilder.toString().uppercase(), 16)
                            if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
                                return l.toInt()
                            } else {
                                return l
                            }
                        }
                    }

                    throw unexpectedEOF()
                } else {
                    input.reset()
                    return parseDecimal(input, '0')
                }
            }

            '.' -> {
                return parseDecimal(input, '.')
            }
        }

        throw unexpected(start.code)
    }

    private fun parseDecimal(input: Reader, c: Char): Number? {
        val stringValueBuilder = StringBuilder().append(c)

        var ch: Int
        input.mark(1)
        while ((input.read().also { ch = it }) != -1) {
            if (Character.isDigit(ch) || ch == '.'.code || ch == 'e'.code || ch == 'E'.code || ch == '+'.code || ch == '-'.code) {
                input.mark(1)
                stringValueBuilder.append(Character.toChars(ch))
            } else {
                input.reset()
                val stringValue = stringValueBuilder.toString()
                if (stringValue.contains(".") || stringValue.contains("e") || stringValue.contains("E")) {
                    return Double.parseDouble(stringValue)
                }

                var number: Number? = null
                try {
                    val bigIntValue = BigInteger(stringValue)
                    number = bigIntValue
                    number = bigIntValue.longValueExact()
                    number = bigIntValue.intValueExact()
                } catch (_: ArithmeticException) {
                }

                return number
            }
        }

        throw unexpectedEOF()
    }

    @Throws(IOException::class)
    private fun skipWhitespace(input: Reader): Boolean {
        input.mark(1)
        var c: Int
        var skipped = 0
        while ((input.read().also { c = it }) != -1) {
            if (!isWhitespace(c) && !isLineTerminator(c)) {
                input.reset()

                return skipped != 0
            }

            skipped++
            input.mark(1)
        }

        throw unexpectedEOF()
    }

    private fun skipComment(input: Reader): Boolean {
        input.mark(2)
        var c = input.read()
        if (c == '/'.code) {
            val c2 = input.read()
            if (c2 == '/'.code) {
                while ((input.read().also { c = it }) != -1 && c != '\n'.code);

                return true
            } else if (c2 == '*'.code) {
                while ((input.read().also { c = it }) != -1) if (c == '*'.code && input.read() == '/'.code) return true

                throw unexpectedEOF()
            } else {
                input.reset()
            }
        } else {
            input.reset()
        }

        return false
    }

    private fun unexpected(ch: Int): IllegalArgumentException {
        return IllegalArgumentException("Unexpected character: " + ch.toChar())
    }

    private fun unexpectedEOF(): IllegalArgumentException {
        return IllegalArgumentException("Unexpected EOF")
    }

    fun unescape(string: String?): String? {
        if (string == null || string.isEmpty()) return string

        val chars = string.toCharArray()
        var j = 0
        var i = 0
        while (i < chars.size) {
            var c = chars[i]

            if (c != '\\') {
                chars[j++] = c
                i++
                continue
            }

            require(i + 1 < chars.size) { "Invalid escape sequence: \\EOS" }

            val d = chars[++i]
            c = when (d) {
                'b' -> '\b'
                'f' -> '\u000c'
                'n' -> '\n'
                'r' -> '\r'
                's' -> ' '
                't' -> '\t'
                '\'', '\"', '\\', '\n', '\r' -> d
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    if (d == '0' && (i + 1 >= chars.size || chars[i + 1] < '0' || chars[i + 1] > '9')) {
                        '\u0000'
                    }
                    val limit = if (d < '4') 2 else 1
                    var code = d.code - '0'.code
                    for (k in 1..<limit) {
                        val e = chars[i + 1]
                        if (e >= '0' && e <= '9') {
                            code = code * 10 + e.code - '0'.code
                            i++
                        }
                    }
                    code.toChar()
                }

                'u' -> {
                    val hex = String(chars, i, 4)
                    require(hex.length == 4) { "Invalid unicode escape: $hex, expected 4 characters, found EOS" }
                    i += 4
                    Integer.parseInt(hex, 16).toChar()
                }

                else -> d
            }

            chars[j++] = c
            i++
        }
        return String(chars, 0, j)
    }
}
