/*
 * generated by Xtext
 */
package org.xspray.xtext.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.JavaIoFileSystemAccess
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess
import org.xspray.generator.graphiti.templates.*
import org.xspray.mm.xspray.*
import org.xspray.generator.graphiti.util.ProjectProperties
import org.xspray.generator.graphiti.util.StringHelpers
import static extension org.xspray.generator.graphiti.util.GeneratorUtil.*
import static extension org.xspray.generator.graphiti.util.MetaModel.*
import static extension org.xspray.generator.graphiti.util.XtendProperties.*
import org.eclipse.internal.xtend.util.StringHelper

class XsprayGenerator implements IGenerator {
	
	/**
	 * This method is a long sequence of calling all templates for the code generation
	 */
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {  
		var JavaIoFileSystemAccess javaFsa 
		var EclipseResourceFileSystemAccess eclipseFsa
		var String modelPath = resource.getURI().devicePath;
		var String propertiesPath = StringHelpers::replaceLastSubstring(modelPath, "xspray", "properties")
		ProjectProperties::setPropertiesFile(propertiesPath);
		var String genOutputPath = ProjectProperties::projectPath + "/" + ProjectProperties::srcGenPath;
		var String manOutputPath = ProjectProperties::projectPath + "/" + ProjectProperties::srcManPath;

		if( fsa instanceof JavaIoFileSystemAccess) {
			javaFsa = (fsa as JavaIoFileSystemAccess) 
		}
		if( fsa instanceof EclipseResourceFileSystemAccess){
			println("EclipseResourceFileSystemAccess: WARNING: dos not work yet")
			eclipseFsa = (fsa as EclipseResourceFileSystemAccess)
		}
		
		var Diagram diagram = resource.contents.head as Diagram

		var Plugin plugin = new Plugin()
		fsa.generateFile("plugin.xml", plugin.generate(diagram))
		
		var JavaGenFile java
		if(  javaFsa != null ){
			java = new JavaGenFile(javaFsa)
			java.setGenOutputPath(genOutputPath)
			java.setManOutputPath(manOutputPath)
		} else  {
			java = new JavaGenFile(eclipseFsa)
		}
		
		java.hasExtensionPoint = true
		java.setPackageAndClass(diagram_package(), diagram.name + "DiagramTypeProvider")
		var DiagramTypeProvider dtp = new DiagramTypeProvider()
		dtp.generate(diagram, java)
		
		java.setPackageAndClass(diagram_package(), diagram.name + "FeatureProvider")
		var FeatureProvider fp = new FeatureProvider()
		fp.generate(diagram, java)
		
		// Generate for all Container Shapes
		for( metaClass : diagram.metaClasses.filter(m | m.representedBy instanceof Container)){
			var container = metaClass.representedBy as Container
			java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Add" + metaClass.visibleName() + "Feature")
			var AddShapeFeature sf = new AddShapeFeature()
			sf.generate(container, java)
		}

		// Generate for all Connection
		for( metaClass : diagram.metaClasses.filter(m | m.representedBy instanceof Connection)){
			var connection = metaClass.representedBy as Connection
			java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Add" + metaClass.visibleName() + "Feature")
			var AddConnectionFeature sf = new AddConnectionFeature()
			sf.generate(metaClass, java)
		}

		// Generate for all EReferences as Connection   TODO metaClass.name ==> metaClass.viibleName()
		for( metaClass : diagram.metaClasses) {
			for( reference : metaClass.references.filter(ref|ref.representedBy != null) ){
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "AddReference" + metaClass.name + reference.name + "Feature")
				var AddReferenceAsConnectionFeature sf = new AddReferenceAsConnectionFeature()
				sf.generate(reference, java)
			}
		}

		for( metaClass : diagram.metaClasses) {
			if( metaClass.representedBy instanceof Container ){
				var container = metaClass.representedBy as Container
				for(metaRef : container.parts.filter(typeof(MetaReference)) ){
					java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Add" + metaClass.name + metaRef.name + "ListFeature")
					var AddReferenceAsListFeature ft = new AddReferenceAsListFeature()
					ft.generate(metaRef, java)
				}
			}
			
		}
		
		for( metaClass : diagram.metaClasses) {
			if( metaClass.representedBy instanceof Connection){
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Create" + metaClass.visibleName() + "Feature")
				var CreateConnectionFeature sf = new CreateConnectionFeature()
				sf.generate(metaClass, java)
			} else {
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Create" + metaClass.visibleName() + "Feature")
				var CreateShapeFeature sf = new CreateShapeFeature()
				sf.generate(metaClass, java)
			}
		}
		
//		println("1 : " +  diagram.metaClasses.filter( m | m.container != null))
		for( reference : diagram.metaClasses.filter( m | m.representedBy != null).map(m | m.representedBy).filter(typeof(Container)).map(c | (c as Container).parts.filter(typeof(MetaReference))).flatten) {
			val referenceName = reference.name
			var metaClass = (reference.eContainer as Container).represents
			var target = metaClass.type.EAllReferences.findFirst(e|e.name == referenceName) 
			var targetType = target.EReferenceType 
			if( ! targetType.abstract){
				println("NOT ABSTRACT: " + targetType.name)
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Create" + metaClass.name + reference.name + targetType.name + "Feature")
				var CreateReferenceAsListFeature ft = new CreateReferenceAsListFeature()
				ft.setTarget(targetType)
				ft.generate(reference, java)
			} else {
				println("ABSTRACT: " + targetType.name)
//				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Create" + metaClass.name + reference.name + targetType.name + "Feature")
//				var CreateReferenceAsListFeature ft = new CreateReferenceAsListFeature()
//				ft.setTarget(targetType)
//				ft.generate(reference, java)
			}
			for( subclass : targetType.getSubclasses() ){
				if( ! subclass.abstract ){
					println("NOT ABSTRACT subclass: " + subclass.name)
					java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Create" + metaClass.name + reference.name + subclass.name + "Feature")
					var CreateReferenceAsListFeature cc = new CreateReferenceAsListFeature()
					cc.setTarget(subclass)
					cc.generate(reference, java)
				} else {
					println("ABSTRACT subclass: " +subclass.name)
					java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Create" + metaClass.name + reference.name + subclass.name + "Feature")
					var CreateReferenceAsListFeature cc = new CreateReferenceAsListFeature()
					cc.setTarget(subclass)
					cc.generate(reference, java)
				}
			}	
		}
		for( metaClass : diagram.metaClasses) {
			for( reference : metaClass.references.filter(ref|ref.representedBy != null) ) {
	    		var CreateReferenceAsConnectionFeature ft = new CreateReferenceAsConnectionFeature() 
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Create" + metaClass.name + reference.name + "Feature")
				ft.generate(reference, java)
		    }
 	    }
 	    
		for( metaClass : diagram.metaClasses) {
			if( metaClass.representedBy instanceof Connection) {
				//    No layout feature needed 
				var UpdateConnectionFeature conn = new UpdateConnectionFeature ()
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Update" + metaClass.visibleName() + "Feature")
				conn.generate(metaClass.representedBy, java)
			} else if( metaClass.representedBy instanceof Container) {
				var LayoutFeature layout = new LayoutFeature()
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Layout" + metaClass.visibleName() + "Feature")
				layout.generate(metaClass.representedBy, java)
				
				var UpdateShapeFeature shape = new UpdateShapeFeature()
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Update" + metaClass.visibleName() + "Feature")
				shape.generate(metaClass.representedBy, java)

				var container = metaClass.representedBy as Container
				for(reference : container.parts.filter(p | p instanceof MetaReference).map(p | p as MetaReference) ){
					val referenceName = reference.name
				    var eClass = metaClass.type.EAllReferences.findFirst(e|e.name == referenceName).EReferenceType 
					var UpdateReferenceAsListFeature list = new UpdateReferenceAsListFeature ()
					list.setTarget(eClass)
					java.setPackageAndClass(feature_package(), diagram.name + "Update" + metaClass.name + reference.name + "Feature")
					list.generate(reference, java)
				}
			}
		}	
		
		for( metaClass : diagram.metaClasses) {
			for( reference : metaClass.references) {
				var DeleteReferenceFeature del = new DeleteReferenceFeature()
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "DeleteReference" + metaClass.name + reference.name + "Feature")
				del.generate(reference, java)
			}
			
		}
		
		java.setPackageAndClass(diagram_package(), diagram.name + "ImageProvider")
		var ImageProvider im = new ImageProvider()
		im.generate(diagram, java)

		java.setPackageAndClass(diagram_package(), diagram.name + "ToolBehaviourProvider")
		var ToolBehaviourProvider  tool = new ToolBehaviourProvider()
		tool.generate(diagram, java)
		
		// PropertySections Java code
		for( metaClass : diagram.metaClasses) {
			val eClass1 = metaClass.type
			for( attribute : eClass1.EAllAttributes){
				java.setPackageAndClass(property_package(), eClass1.name + attribute.name.toFirstUpper() + "Section")
				var PropertySection section = new PropertySection()
				section.setDiagram(diagram)
				section.generate(attribute, java)
			}
			if( metaClass.representedBy instanceof Container ){
				val container = metaClass.representedBy as Container
				for( reference : container.parts.filter(p | p instanceof MetaReference).map(p | p as MetaReference)) {
					val referenceName = reference.name
					var eClass = metaClass.type.EAllReferences.findFirst(r | r.name == referenceName).EReferenceType
					for( attribute : eClass.EAllAttributes ){
						java.setPackageAndClass(property_package(), eClass.name + attribute.name.toFirstUpper() + "Section")
						var PropertySection section = new PropertySection()
						section.setDiagram(diagram)
						section.generate(attribute, java)
					}
				}
			}
		}		
		
		for( metaClass : diagram.metaClasses) {
			val Filter fil = new Filter()
			fil.setDiagram(diagram)
			java.setPackageAndClass(property_package(), metaClass.name + "Filter")
			fil.generate(metaClass.type, java)

			if( metaClass.representedBy instanceof Container){
				val container = metaClass.representedBy as Container
				for( reference : container.parts.filter( p | p instanceof MetaReference).map(p | p as MetaReference)){
					val referenceName = reference.name
					val eClass = metaClass.type.EAllReferences.findFirst(ref| ref.name == referenceName).EReferenceType 
					val Filter fil2 = new Filter()
					fil2.setDiagram(diagram)
					java.setPackageAndClass(property_package(), eClass.name + "Filter")
					fil2.generate(eClass, java)
				}
			}
		}

		for( metaClass : diagram.metaClasses) {
			for( behaviour : metaClass.behaviours){
				val CustomFeature custom = new CustomFeature ()
				java.setPackageAndClass(feature_package(), metaClass.diagram.name + "Custom" + behaviour.name.toFirstUpper() + "Feature")
				custom.generate(behaviour, java)
			}
		}
	}


}
