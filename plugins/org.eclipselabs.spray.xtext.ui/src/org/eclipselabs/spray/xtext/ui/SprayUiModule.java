/*
 * generated by Xtext
 */
package org.eclipselabs.spray.xtext.ui;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.common.types.xtext.ui.ITypesProposalProvider;
import org.eclipse.xtext.common.ui.contentassist.TerminalsProposalProvider;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.service.SingletonBinding;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.model.TokenTypeToStringMapper;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.util.PluginProjectFactory;
import org.eclipse.xtext.ui.wizard.IProjectCreator;
import org.eclipselabs.spray.shapes.ui.hover.ImageResourceVisitor;
import org.eclipselabs.spray.shapes.ui.linking.connection.ConnectionEditorOpener;
import org.eclipselabs.spray.shapes.ui.linking.connection.ConnectionLinkingHelper;
import org.eclipselabs.spray.shapes.ui.linking.connection.ConnectionResourceVisitor;
import org.eclipselabs.spray.shapes.ui.linking.shape.ShapeEditorOpener;
import org.eclipselabs.spray.shapes.ui.linking.shape.ShapeLinkingHelper;
import org.eclipselabs.spray.shapes.ui.linking.shape.ShapeResourceVisitor;
import org.eclipselabs.spray.styles.ui.linking.style.StyleEditorOpener;
import org.eclipselabs.spray.styles.ui.linking.style.StyleLinkingHelper;
import org.eclipselabs.spray.styles.ui.linking.style.StyleResourceVisitor;
import org.eclipselabs.spray.xtext.generator.outputconfig.SprayOutputConfigurationProvider;
import org.eclipselabs.spray.xtext.ui.builder.SprayBuilderParticipant;
import org.eclipselabs.spray.xtext.ui.builder.SprayResourceDescriptionManager;
import org.eclipselabs.spray.xtext.ui.contentassist.SprayJdtTypesProposalPriorities;
import org.eclipselabs.spray.xtext.ui.contentassist.SprayJdtTypesProposalProvider;
import org.eclipselabs.spray.xtext.ui.hover.SprayEObjectHoverProvider;
import org.eclipselabs.spray.xtext.ui.hover.SprayEObjectTextHover;
import org.eclipselabs.spray.xtext.ui.linking.SprayDispatchingLinkingHelper;
import org.eclipselabs.spray.xtext.ui.linking.domain.DomainEditorOpener;
import org.eclipselabs.spray.xtext.ui.linking.domain.DomainLinkingHelper;
import org.eclipselabs.spray.xtext.ui.linking.domain.DomainResourceVisitor;
import org.eclipselabs.spray.xtext.ui.syntaxcoloring.SprayHighlightingConfiguration;
import org.eclipselabs.spray.xtext.ui.syntaxcoloring.SprayTokenToAttributeIdMapper;
import org.eclipselabs.spray.xtext.ui.validation.SprayJavaUIValidator;
import org.eclipselabs.spray.xtext.ui.wizard.SprayPluginProjectFactory;
import org.eclipselabs.spray.xtext.ui.wizard.SprayProjectCreator;
import org.eclipselabs.spray.xtext.validation.SprayJavaValidator;

import com.google.common.base.Predicate;
import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Use this class to register components to be used within the IDE.
 */
public class SprayUiModule extends AbstractSprayUiModule {
    /** Key for String Binding */
    public static final String NEW_PROJECT_NAME = "org.eclipselabs.spray.xtext.ui.newProjectName";

    public SprayUiModule(AbstractUIPlugin plugin) {
        super(plugin);
    }

    public Class<? extends org.eclipse.xtext.generator.IOutputConfigurationProvider> bindIOutputConfigurationProvider() {
        return SprayOutputConfigurationProvider.class;
    }

    public Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindAbstractAntlrTokenToAttributeIdMapper() {
        return SprayTokenToAttributeIdMapper.class;
    }

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(TokenTypeToStringMapper.class).to(SprayTokenToAttributeIdMapper.class);
        binder.bind(Predicate.class).annotatedWith(Names.named("ePackageUriFilter")).toInstance(new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                if (input.startsWith("http://www.eclipse.org/emf") && !input.equals(EcorePackage.eNS_URI))
                    return true;
                return false;
            }
        });
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.IContentProposalPriorities> bindIContentProposalPriorities() {
        return SprayJdtTypesProposalPriorities.class;
    }

    public void configureNewProjectName(Binder binder) {
        binder.bind(String.class).annotatedWith(Names.named(NEW_PROJECT_NAME)).toInstance("org.eclipselabs.spray.examples.mydiagram");
    }

    public Class<? extends IProjectCreator> bindIProjectCreator() {
        return SprayProjectCreator.class;
    }

    public Class<? extends PluginProjectFactory> bindPluginProjectFactory() {
        return SprayPluginProjectFactory.class;
    }

    @SingletonBinding(eager = true)
    public Class<? extends SprayJavaValidator> bindSprayJavaValidator() {
        return SprayJavaUIValidator.class;
    }

    /**
     * Usually contributed by org.eclipse.xtext.generator.generator.GeneratorFragment,
     * but this fragment is not used. The SprayProjectCreator needs it.
     */
    public org.eclipse.core.resources.IWorkspaceRoot bindIWorkspaceRootToInstance() {
        return org.eclipse.core.resources.ResourcesPlugin.getWorkspace().getRoot();
    }

    @Override
    public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
        return SprayHighlightingConfiguration.class;
    }

    @Override
    public Class<? extends ITypesProposalProvider> bindITypesProposalProvider() {
        return SprayJdtTypesProposalProvider.class;
    }

    @Override
    public Class<? extends org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper> bindIHyperlinkHelper() {
        return SprayDispatchingLinkingHelper.class;
    }

    public Class<? extends StyleLinkingHelper> bindStyleLinkingHelper() {
        return StyleLinkingHelper.class;
    }

    public Class<? extends StyleEditorOpener> bindStyleEditorOpener() {
        return StyleEditorOpener.class;
    }

    public Class<? extends StyleResourceVisitor> bindStyleResourceVisitor() {
        return StyleResourceVisitor.class;
    }

    public Class<? extends ShapeLinkingHelper> bindShapeLinkingHelper() {
        return ShapeLinkingHelper.class;
    }

    public Class<? extends ShapeEditorOpener> bindShapeEditorOpener() {
        return ShapeEditorOpener.class;
    }

    public Class<? extends ShapeResourceVisitor> bindShapeResourceVisitor() {
        return ShapeResourceVisitor.class;
    }

    public Class<? extends ConnectionLinkingHelper> bindConnectionLinkingHelper() {
        return ConnectionLinkingHelper.class;
    }

    public Class<? extends ConnectionEditorOpener> bindConnectionEditorOpener() {
        return ConnectionEditorOpener.class;
    }

    public Class<? extends ConnectionResourceVisitor> bindConnectionResourceVisitor() {
        return ConnectionResourceVisitor.class;
    }

    public Class<? extends DomainLinkingHelper> bindDomainLinkingHelper() {
        return DomainLinkingHelper.class;
    }

    public Class<? extends DomainEditorOpener> bindDomainEditorOpener() {
        return DomainEditorOpener.class;
    }

    public Class<? extends DomainResourceVisitor> bindDomainResourceVisitor() {
        return DomainResourceVisitor.class;
    }

    @Override
    public Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider() {
        return SprayEObjectHoverProvider.class;
    }

    public Class<? extends ImageResourceVisitor> bindImageResourceVisitor() {
        return ImageResourceVisitor.class;
    }

    @Override
    public Class<? extends org.eclipse.xtext.ui.editor.hover.IEObjectHover> bindIEObjectHover() {
        return SprayEObjectTextHover.class;
    }

    public Class<? extends IResourceDescription.Manager> bindIResourceDescription$Manager() {
        return SprayResourceDescriptionManager.class;
    }

    public Class<? extends org.eclipse.xtext.builder.IXtextBuilderParticipant> bindIXtextBuilderParticipant() {
        return SprayBuilderParticipant.class;
    }

    public Class<? extends TerminalsProposalProvider> bindTerminalsProposalProvider() {
        return TerminalsProposalProvider.class;
    }
}
