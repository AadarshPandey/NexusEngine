package com.nexusengine.core.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String resourceType;
    private final Object identifier;

    public ResourceNotFoundException(String resourceType, Object identifier) {
        super(resourceType + " not found with id: " + identifier);
        this.resourceType = resourceType;
        this.identifier = identifier;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceType = "Resource";
        this.identifier = null;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Object getIdentifier() {
        return identifier;
    }
}
