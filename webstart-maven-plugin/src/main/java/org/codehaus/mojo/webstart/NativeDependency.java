package org.codehaus.mojo.webstart;

import java.util.List;

public class NativeDependency {

	private String os;

	private String arch;

	private List<NativeResource> nativeResources;

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public List<NativeResource> getNativeResources() {
		return nativeResources;
	}

	public void setNativeResources(List<NativeResource> nativeResources) {
		this.nativeResources = nativeResources;
	}

}
