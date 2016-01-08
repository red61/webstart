package org.codehaus.mojo.webstart;

import org.apache.maven.artifact.Artifact;

public abstract class JnlpResource {

	protected String groupId;

	protected String artifactId;

	protected String version;

	protected String classifier;

	protected String type;
	
	protected Artifact artifact;
	
	protected String hrefValue;
	
	/**
	 * Returns the value of the artifactId field.
	 * 
	 * @return Returns the value of the artifactId field.
	 */
	public String getArtifactId() {
		return this.artifactId;
	}

	/**
	 * Returns the value of the type field.
	 * 
	 * @return Returns the value of the type field.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Returns the value of the classifier field.
	 * 
	 * @return Returns the value of the classifier field.
	 */
	public String getClassifier() {
		return this.classifier;
	}

	/**
	 * Returns the value of the groupId field.
	 * 
	 * @return Returns the value of the groupId field.
	 */
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * Returns the value of the version field.
	 * 
	 * @return Returns the value of the version field.
	 */
	public String getVersion() {
		return this.version;
	}
	
	/**
     * Sets the value that should be output for this jar in the href attribute of the
     * jar resource element in the generated JNLP file. If not set explicitly, this defaults
     * to the file name of the underlying artifact.
     *
     * @param hrefValue
     */
    protected void setHrefValue( String hrefValue )
    {
        this.hrefValue = hrefValue;
    }

	/**
	 * Returns true if the given object is a JnlpResource and has the same
	 * combination of <code>groupId</code>, <code>artifactId</code>,
	 * <code>version</code> and <code>classifier</code>.
	 */
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof JnlpResource)) {
			return false;
		}

		JnlpResource other = (JnlpResource) obj;

		if (fieldsAreNotEqual(getGroupId(), other.getGroupId())) {
			return false;
		}

		if (fieldsAreNotEqual(getArtifactId(), other.getArtifactId())) {
			return false;
		}

		if (fieldsAreNotEqual(getVersion(), other.getVersion())) {
			return false;
		}

		if (fieldsAreNotEqual(getType(), other.getType())) {
			return false;
		}

		if (fieldsAreNotEqual(getClassifier(), other.getClassifier())) {
			return false;
		}

		return true;

	}

	private boolean fieldsAreNotEqual(Object field1, Object field2) {

		if (field1 == null) {
			return field2 != null;
		} else {
			return !field1.equals(field2);
		}

	}
	
	/**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        final int offset = 17;
        final int multiplier = 37;
        int result = offset;
        result += multiplier * fieldHashCode( getGroupId() );
        result += multiplier * fieldHashCode( getArtifactId() );
        result += multiplier * fieldHashCode( getVersion() );
        result += multiplier * fieldHashCode( getType() );
        result += multiplier * fieldHashCode( getClassifier() );
        return result;

    }

    private int fieldHashCode( Object field )
    {
        return field == null ? 0 : field.hashCode();
    }
    
	/**
     * Returns the value that should be output for this jar in the href attribute of the
     * jar resource element in the generated JNLP file. If not set explicitly, this defaults
     * to the file name of the underlying artifact.
     *
     * @return The href attribute to be output for this jar resource in the generated JNLP file.
     */
    public String getHrefValue()
    {
        if ( this.hrefValue == null && this.artifact != null )
        {
            return this.artifact.getFile().getName();
        }
        return this.hrefValue;
    }
    
    /**
     * Returns the underlying artifact that this instance represents.
     *
     * @return Returns the value of the artifact field.
     */
    public Artifact getArtifact()
    {
        return this.artifact;
    }

    /**
     * Sets the underlying artifact that this instance represents.
     *
     * @param artifact
     * @throws IllegalArgumentException if {@code artifact} is null.
     */
    public void setArtifact( Artifact artifact )
    {
        if ( artifact == null )
        {
            throw new IllegalArgumentException( "artifact must not be null" );
        }
        this.artifact = artifact;
        this.artifactId = artifact.getArtifactId();
        this.type = artifact.getType();
        this.classifier = artifact.getClassifier();
        this.groupId = artifact.getGroupId();
        if(artifact.isSnapshot()) {
            this.version = artifact.getFile().lastModified() + "";
        }
        else {
            this.version = artifact.getVersion();
        }
    }

	public abstract boolean isOutputVersion();
}
