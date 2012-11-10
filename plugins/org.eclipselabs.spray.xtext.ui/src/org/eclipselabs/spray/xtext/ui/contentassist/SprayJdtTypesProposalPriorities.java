package org.eclipselabs.spray.xtext.ui.contentassist;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentProposalPriorities;

public class SprayJdtTypesProposalPriorities extends ContentProposalPriorities {

    @Override
    protected void adjustPriority(ICompletionProposal proposal, String prefix, int priority) {
        if (proposal == null || !(proposal instanceof ConfigurableCompletionProposal)) {
            return;
        }
        if (!proposal.getDisplayString().contains(".")) {
            super.adjustPriority(proposal, prefix, priority * proposalWithPrefixMultiplier);
        }
        super.adjustPriority(proposal, prefix, priority);
    }
}
