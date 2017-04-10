/*
 * The MIT License
 *
 * Copyright (c) 2017 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jenkins.scm.api;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Represents the origin of a {@link SCMHead}.
 * <ul>
 * <li>For centralized version control systems, this will always be {@link #DEFAULT}</li>
 * <li>For distributed version control systems, the distributed nature allows for other origins. It is expected that
 * {@link SCMSource} implementations that support {@link SCMHead} instances coming from a different origin will solve
 * the problem of describing the alternative origins by subclassing {@link SCMHeadOrigin} appropriately (assuming
 * {@link Fork} is not applicable).</li>
 * <li>For the centralized distributed version control systems such as GitHub, Bitbucket, etc there is
 * a standard concept of a named {@link Fork}.
 * </li>
 * </ul>
 *
 * @since 2.2.0
 */
public abstract class SCMHeadOrigin implements Serializable {

    /**
     * The default {@link SCMHeadOrigin}.
     */
    public static final SCMHeadOrigin.Default DEFAULT = new SCMHeadOrigin.Default();

    /**
     * Standardize serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int hashCode();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String toString();

    /**
     * The default origin.
     */
    public static final class Default extends SCMHeadOrigin {
        /**
         * Standardize serialization.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor.
         */
        private Default() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            return o instanceof Default;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Default.class.hashCode();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Default";
        }

        /**
         * Ensure this is a singleton after deserialization.
         *
         * @return the singleton.
         * @throws ObjectStreamException never.
         */
        private Object readResolve() throws ObjectStreamException {
            return DEFAULT;
        }
    }

    /**
     * A named fork. The name should be simple not an URL.
     */
    public static class Fork extends SCMHeadOrigin {
        /**
         * Standardize serialization.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The name of the fork. This should
         */
        @NonNull
        private final String name;

        /**
         * Creates a fork origin instance.
         *
         * @param name the name.
         */
        public Fork(@NonNull String name) {
            this.name = name;
        }

        /**
         * Gets the name of this fork.
         *
         * @return the name of this fork.
         */
        @NonNull
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Fork fork = (Fork) o;

            return name.equals(fork.name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return name.hashCode();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Fork[" +
                    "name='" + name + '\'' +
                    ']';
        }
    }
}
