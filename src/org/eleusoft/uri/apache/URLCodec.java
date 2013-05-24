/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package org.apache.commons.codec.net;
package org.eleusoft.uri.apache;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import org.apache.commons.codec.EncoderException;

/**
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CodecException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
**/

/**
 * <p>Implements the 'www-form-urlencoded' encoding scheme,
 * also misleadingly known as URL encoding.</p>
 *
 * <p>For more detailed information please refer to
 * <a href="http://www.w3.org/TR/html4/interact/forms.html#h-17.13.4.1">
 * Chapter 17.13.4 'Form content types'</a> of the
 * <a href="http://www.w3.org/TR/html4/">HTML 4.01 Specification<a></p>
 *
 * <p>
 * This codec is meant to be a replacement for standard Java classes
 * {@link java.net.URLEncoder} and {@link java.net.URLDecoder}
 * on older Java platforms, as these classes in Java versions below
 * 1.4 rely on the platform's default charset encoding.
 * </p>
 *
 * @author Apache Software Foundation
 * @since 1.2
 * @version $Id: URLCodec.java,v 1.1 2007/07/10 18:19:30 mik Exp $
 */
public class URLCodec //implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
{

    /**
     * The default charset used for string decoding and encoding.
     */
    protected String charset = "UTF-8";
    
    private static final String US_ASCII = "US-ASCII";

    protected static final byte ESCAPE_CHAR = '%';
    /**
     * BitSet of www-form-url safe characters.
     */
    protected static final BitSet WWW_FORM_URL = new BitSet(256);

    // Static initializer for www_form_url
    static {
        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            WWW_FORM_URL.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            WWW_FORM_URL.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            WWW_FORM_URL.set(i);
        }
        // special chars
        WWW_FORM_URL.set('-');
        WWW_FORM_URL.set('_');
        WWW_FORM_URL.set('.');
        WWW_FORM_URL.set('*');
        // blank to be replaced with +
        WWW_FORM_URL.set(' ');
    }


    /**
     * Default constructor.
     */
    public URLCodec() {
        super();
    }

    /**
     * Constructor which allows for the selection of a default charset
     *
     * @param charset the default string charset to use.
     */
    public URLCodec(String charset) {
        super();
        this.charset = charset;
    }

    /**
     * Encodes an array of bytes into an array of URL safe 7-bit
     * characters. Unsafe characters are escaped.
     *
     * @param urlsafe bitset of characters deemed URL safe
     * @param bytes array of bytes to convert to URL safe characters
     * @return array of bytes containing URL safe characters
     */
    public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes)
    {
        if (bytes == null) {
            return null;
        }
        if (urlsafe == null) {
            urlsafe = WWW_FORM_URL;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];
            if (b < 0) {
                b = 256 + b;
            }
            if (urlsafe.get(b)) {
                if (b == ' ') {
                    b = '+';
                }
                buffer.write(b);
            } else {
                buffer.write('%');
                char hex1 = Character.toUpperCase(
                  Character.forDigit((b >> 4) & 0xF, 16));
                char hex2 = Character.toUpperCase(
                  Character.forDigit(b & 0xF, 16));
                buffer.write(hex1);
                buffer.write(hex2);
            }
        }
        return buffer.toByteArray();
    }


    /**
     * Decodes an array of URL safe 7-bit characters into an array of
     * original bytes. Escaped characters are converted back to their
     * original representation.
     *
     * @param bytes array of URL safe characters
     * @return array of original bytes
     * @throws CodecException Thrown if URL decoding is unsuccessful
     */
    public static final byte[] decodeUrl(byte[] bytes)
         throws CodecException
    {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];
            if (b == '+') {
                buffer.write(' ');
            } else if (b == '%') {
                try {
                    int u = Character.digit((char)bytes[++i], 16);
                    int l = Character.digit((char)bytes[++i], 16);
                    if (u == -1 || l == -1) {
                        throw new CodecException("Invalid URL encoding");
                    }
                    buffer.write((char)((u << 4) + l));
                } catch(ArrayIndexOutOfBoundsException e) {
                    throw new CodecException("Invalid URL encoding");
                }
            } else {
                buffer.write(b);
            }
        }
        return buffer.toByteArray();
    }


    /**
     * Encodes an array of bytes into an array of URL safe 7-bit
     * characters. Unsafe characters are escaped.
     *
     * @param bytes array of bytes to convert to URL safe characters
     * @return array of bytes containing URL safe characters
     */
    public byte[] encode(byte[] bytes) {
        return encodeUrl(WWW_FORM_URL, bytes);
    }


    /**
     * Decodes an array of URL safe 7-bit characters into an array of
     * original bytes. Escaped characters are converted back to their
     * original representation.
     *
     * @param bytes array of URL safe characters
     * @return array of original bytes
     * @throws CodecException Thrown if URL decoding is unsuccessful
     */
    public byte[] decode(byte[] bytes) throws CodecException {
        return decodeUrl(bytes);
    }


    /**
     * Encodes a string into its URL safe form using the specified
     * string charset. Unsafe characters are escaped.
     *
     * @param pString string to convert to a URL safe form
     * @param charset the charset for pString
     * @return URL safe string
     * @throws UnsupportedEncodingException Thrown if charset is not
     *                                      supported
     */
    public String encode(String pString, String charset)
        throws UnsupportedEncodingException
    {
        if (pString == null) {
            return null;
        }
        return new String(encode(pString.getBytes(charset)), US_ASCII);//StringEncodings.US_ASCII
    }


    /**
     * Encodes a string into its URL safe form using the default string
     * charset. Unsafe characters are escaped.
     *
     * @param pString string to convert to a URL safe form
     * @return URL safe string
     * @throws EncoderException Thrown if URL encoding is unsuccessful
     *
     * @see #getDefaultCharset()
     */
    public String encode(String pString) 
        throws CodecException, UnsupportedEncodingException {
        if (pString == null) {
            return null;
        }
        return encode(pString, getDefaultCharset());
        
    }


    /**
     * Decodes a URL safe string into its original form using the
     * specified encoding. Escaped characters are converted back
     * to their original representation.
     *
     * @param pString URL safe string to convert into its original form
     * @param charset the original string charset
     * @return original string
     * @throws CodecException Thrown if URL decoding is unsuccessful
     * @throws UnsupportedEncodingException Thrown if charset is not
     *                                      supported
     */
    public String decode(String pString, String charset)
        throws CodecException, UnsupportedEncodingException
    {
        if (pString == null) {
            return null;
        }
        return new String(decode(pString.getBytes(US_ASCII)), charset);
    }


    /**
     * Decodes a URL safe string into its original form using the default
     * string charset. Escaped characters are converted back to their
     * original representation.
     *
     * @param pString URL safe string to convert into its original form
     * @return original string
     * @throws CodecException Thrown if URL decoding is unsuccessful
     *
     * @see #getDefaultCharset()
     */
    public String decode(String pString) throws CodecException {
        if (pString == null) {
            return null;
        }
        try {
            return decode(pString, getDefaultCharset());
        } catch(UnsupportedEncodingException e) {
            throw new CodecException(e.getMessage());
        }
    }

    
    /**
     * The <code>String</code> encoding used for decoding and encoding.
     *
     * @return Returns the encoding.
     *
     * @deprecated use #getDefaultCharset()
     */
    public String getEncoding() {
        return this.charset;
    }

    /**
     * The default charset used for string decoding and encoding.
     *
     * @return the default string charset.
     */
    public String getDefaultCharset() {
        return this.charset;
    }

}
