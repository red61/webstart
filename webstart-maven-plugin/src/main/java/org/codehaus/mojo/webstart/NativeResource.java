package org.codehaus.mojo.webstart;

public class NativeResource extends JnlpResource {
	private String resourceType;
	private boolean outputVersion = true;

	public String getResourceType() {
		return resourceType;
	}

	public boolean isOutputVersion() {
		return outputVersion;
	}
    
    @Override
    public String toString()
    {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append( "NativeResource[ groupId='" ).append( this.groupId ).append( "', artifactId='" ).append(
            this.artifactId ).append( "', version='" ).append( this.version ).append( "', classifier='" ).append(
            this.classifier ).append( "', resourceType='" ).append( this.resourceType ).append(
            "', outputVersion='" ).append( this.outputVersion ).append( "', hrefValue='" ).append(
            this.hrefValue ).append( "' ]" );
        return sbuf.toString();
    }
}
