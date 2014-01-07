/*
 * $Header: /cvsrep/fd/WEB-INF/classes/org/eleusoft/uri/apache/URIException.java,v 1.1 2007/07/10 18:19:30 mik Exp $
 * $Revision: 1.1 $
 * $Date: 2007/07/10 18:19:30 $
 *
 * ====================================================================
 *
 *  Copyright 2002-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

//package org.apache.commons.httpclient;
package org.eleusoft.uri.apache;

/**
 * The URI parsing and escape encoding exception.
 *
 * @author <a href="mailto:jericho at apache.org">Sung-Gu</a>
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * @version $Revision: 1.1 $ $Date: 2002/03/14 15:14:01
 */
class URIException
	//extends HttpException
	extends java.io.IOException {

    // ----------------------------------------------------------- constructors

    /**
     * Default constructor.
     */
    private URIException() {
    }


    /**
     * The constructor with a reason string and its code arguments.
     *
     * @param reasonCode the reason code
     * @param reason the reason
     */
    public URIException(int reasonCode, String reason) {
        super(reason); 
        this.reasonCode = reasonCode;
    }


    /**
     * The constructor with a reason string argument.
     *
     * @param reason the reason
     */
    public URIException(String reason) {
        super(reason); 
        this.reasonCode = UNKNOWN;
    }

    // -------------------------------------------------------------- constants

    /**
     * No specified reason code.
     */
    public static final int UNKNOWN = 0;


    /**
     * The URI parsing error.
     */
    public static final int PARSING = 1;


    /**
     * The unsupported character encoding.
     */
    public static final int UNSUPPORTED_ENCODING = 2;


    /**
     * The URI escape encoding and decoding error.
     */
    public static final int ESCAPING = 3;


    /**
     * The DNS punycode encoding or decoding error.
     */
    public static final int PUNYCODE = 4;

    // ------------------------------------------------------------- properties

    /**
     * The reason code.
     */
    protected int reasonCode;


    /**
     * The reason message.
     */
    protected String reason;

    // ---------------------------------------------------------------- methods

    /**
     * Get the reason code.
     *
     * @return the reason code
     */
    public int getReasonCode() {
        return reasonCode;
    }




}

