package alice.tuprologx.eclipse.toolbar;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import alice.tuprologx.eclipse.wizards.PrologWizard;

public class PrologTheoryAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
	};

	public void init(IWorkbenchWindow window) {
	};

	public void run(IAction action) {
		// MessageDialog.openInformation(null,null,"HelloWorld");
		// Create the wizard
		PrologWizard wizard = new PrologWizard();
		IStructuredSelection selection = null;
		wizard.init(PlatformUI.getWorkbench(), selection);

		// Create the wizard dialog
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		// Open the wizard dialog
		dialog.open();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}
