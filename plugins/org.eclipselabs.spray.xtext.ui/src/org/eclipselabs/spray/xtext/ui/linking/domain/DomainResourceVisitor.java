package org.eclipselabs.spray.xtext.ui.linking.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;
import org.eclipse.xtext.common.types.xtext.ui.XtextResourceSetBasedProjectProvider;
import org.eclipselabs.spray.runtime.xtext.ui.linking.DSLResourceVisitor;

import com.google.inject.Inject;

public class DomainResourceVisitor extends DSLResourceVisitor<EObject> {
    private static final String  ECORE_FILEEXTENSION = "ecore";

    @Inject
    private IJavaProjectProvider javaProjectProvider;

    @Override
    public void fillFileToEObjects(IResource resource, EObject root, Map<IResource, List<EObject>> fileToEObjects) {
        List<EObject> list;
        TreeIterator<EObject> iterator = root.eAllContents();
        EObject ele;
        while (iterator.hasNext()) {
            ele = iterator.next();
            list = fileToEObjects.get(resource);
            if (list == null) {
                list = new ArrayList<EObject>();
            }
            list.add((EObject) ele);
            fileToEObjects.put(resource, list);
        }
    }

    @Override
    protected String getFileExtension() {
        return ECORE_FILEEXTENSION;
    }

    @Override
    protected IJavaProjectProvider getJavaProjectProvider() {
        if (javaProjectProvider == null) {
            return new XtextResourceSetBasedProjectProvider();
        }
        return javaProjectProvider;
    }
}
