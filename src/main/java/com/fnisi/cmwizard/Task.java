package com.fnisi.cmwizard;

import javax.swing.*;

class Task extends SwingWorker<Void, Void> {

    @Override
    protected Void doInBackground() throws Exception {
        return null;
    }

    @Override
    protected void done() {
        System.out.println("DONE");
    }
}

