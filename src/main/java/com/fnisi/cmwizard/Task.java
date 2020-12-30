package com.fnisi.cmwizard;

import javax.swing.*;
import java.io.File;

class Task extends SwingWorker<Void, Void> {
    private File file;
    private CMWizardGui cmWizardGui;

    public Task(File file) {
        this.file = file;
        this.cmWizardGui = null;
    }

    public void setCmWizardGui(CMWizardGui c) {
        this.cmWizardGui = c;
    }

    @Override
    protected Void doInBackground() throws Exception {
        int progress = 0, totalRecords;

        setProgress(0);
        CmXmlReader cmXmlReader = new CmXmlReader(file);
        TableCreator tc = new TableCreator(cmXmlReader);
        tc.setTask(this);
        tc.createTabs();
        return null;
    }

    @Override
    protected void done() {
        if (cmWizardGui != null) {
            cmWizardGui.setStatusLabel("Completed!");
        }
    }

    public void updateProgress(int p) {
        setProgress(p);
    }
}

