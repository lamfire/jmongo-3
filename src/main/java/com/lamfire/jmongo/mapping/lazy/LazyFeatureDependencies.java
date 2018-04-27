package com.lamfire.jmongo.mapping.lazy;


import com.lamfire.jmongo.logging.JmongoLoggerFactory;
import com.lamfire.jmongo.logging.Logger;



public final class LazyFeatureDependencies {

    private static final Logger LOG = JmongoLoggerFactory.get(LazyFeatureDependencies.class);
    private static Boolean fulFilled;

    private LazyFeatureDependencies() {
    }


    public static boolean assertDependencyFullFilled() {
        final boolean fulfilled = testDependencyFullFilled();
        if (!fulfilled) {
            LOG.warning("Lazy loading impossible due to missing dependencies.");
        }
        return fulfilled;
    }


    public static boolean testDependencyFullFilled() {
        if (fulFilled != null) {
            return fulFilled;
        }
        try {
            fulFilled = Class.forName("net.sf.cglib.proxy.Enhancer") != null
                        && Class.forName("com.thoughtworks.proxy.toys.hotswap.HotSwapping")
                           != null;
        } catch (ClassNotFoundException e) {
            fulFilled = false;
        }
        return fulFilled;
    }


    public static LazyProxyFactory createDefaultProxyFactory() {
        if (testDependencyFullFilled()) {
            final String factoryClassName = "org.mongodb.mapping.mapping.lazy.CGLibLazyProxyFactory";
            try {
                return (LazyProxyFactory) Class.forName(factoryClassName).newInstance();
            } catch (Exception e) {
                LOG.error("While instantiating " + factoryClassName, e);
            }
        }
        return null;
    }
}
