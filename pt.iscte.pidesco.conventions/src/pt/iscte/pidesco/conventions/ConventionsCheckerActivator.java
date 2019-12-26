package pt.iscte.pidesco.conventions;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ConventionsCheckerActivator implements BundleActivator {

	private static BundleContext context;
	private static ConventionsCheckerActivator instance;

	static BundleContext getContext() {
		return context;
	}

	public static ConventionsCheckerActivator getInstance() {
		return instance;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		instance = this;
		ConventionsCheckerActivator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		ConventionsCheckerActivator.context = null;
	}

}
