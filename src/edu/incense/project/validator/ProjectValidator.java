package edu.incense.project.validator;

import edu.incense.project.ProjectSignature;

public interface ProjectValidator {
    public boolean isValid(ProjectSignature projectSignature);
}
