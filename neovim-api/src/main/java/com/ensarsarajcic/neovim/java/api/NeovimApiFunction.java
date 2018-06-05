/*
 * MIT License
 *
 * Copyright (c) 2018 Ensar Sarajčić
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ensarsarajcic.neovim.java.api;

import java.lang.annotation.*;

/**
 * Annotation marking that method is a NeovimApiFunction
 * Marking with this annotation means that the method will do the actual call
 *
 * It is useful for documentation since it contains information about versions, params and name
 * It could also be used for generating clients and for compile time checking
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NeovimApiFunction {
    /**
     * Name of the function/method
     * This is the name used to call the function via RPC
     */
    String name() default "";

    /**
     * Version of Neovim API the function/method was introduced in
     * It is used for documentation
     * It can also be used for checking if method is supported by current version
     */
    int since() default 0;

    /**
     * Version of Neovim API the function/method was deprecated in
     * It is used for documentation
     * It can also be used for checking if method is supported by current version
     * 0 means it is not deprecated
     */
    int deprecatedIn() default 0;

    /**
     * If function/method is deprecated, this can be used to point to function that should be used instead
     */
    String useFunction() default "";
}
