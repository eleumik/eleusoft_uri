/*
 * $Header: /cvsrep/fd/WEB-INF/classes/org/eleusoft/uri/apache/HttpClientError.java,v 1.1 2007/07/10 18:19:30 mik Exp $
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
 * Signals that an error has occurred.
 *
 * @author Ortwin Gl?ck
 * @version $Revision: 1.1 $ $Date: 2007/07/10 18:19:30 $
 * @since 3.0
 */
class HttpClientError extends Error {

    /**
     * Creates a new HttpClientError with a <tt>null</tt> detail message.
     */
    public HttpClientError() {
        super();
    }

    /**
     * Creates a new HttpClientError with the specified detail message.
     * @param message The error message
     */
    public HttpClientError(String message) {
        super(message);
    }

}