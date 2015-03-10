/*
 * Copyright 2014 CyberVision, Inc.
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

package org.kaaproject.kaa.server.control.service.thrift;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.kaaproject.kaa.server.common.thrift.gen.control.ControlThriftException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ControlThriftServiceAspect.
 */
@Aspect
public class ControlThriftServiceAspect {

    /** The Constant logger. */
    private static final Logger LOG = LoggerFactory
            .getLogger(ControlThriftServiceAspect.class);

    /**
     * Do control sevice method.
     * 
     * @param pjp
     *            the Proceeding Join Point
     * @return the resulting method object
     * @throws Throwable
     *             the throwable
     */
    @Around("execution(* org.kaaproject.kaa.server.common.thrift.gen.control.ControlThriftService.Iface.*(..))")
    public Object doControlSeviceMethod(ProceedingJoinPoint pjp)
            throws Throwable { //NOSONAR
        Object retVal = null;
        try {
            retVal = pjp.proceed();
        } catch (Exception t) {
            LOG.error(MessageFormat.format("Unhandled exception: {0}", t.getMessage()), t);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(outStream);
            t.printStackTrace(writer); //NOSONAR
            writer.flush();
            throw new ControlThriftException(
                    t.getMessage(), t.getClass().getName(),
                    outStream.toString());
        }
        return retVal;
    }

}
