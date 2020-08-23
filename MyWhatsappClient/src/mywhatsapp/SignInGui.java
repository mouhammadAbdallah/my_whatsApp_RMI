/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mywhatsapp;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Moham
 */
public class SignInGui extends javax.swing.JFrame {

    /**
     * Creates new form SignInGui
     */
    IMyWhatsappServer serverRef;
    public ClientGui whatsapp;

    public SignInGui() {
        initComponents();
        setVisible(true);
        setSize(620, 490);
        try {
            serverRef = (IMyWhatsappServer) Naming.lookup(MyWhatsappClient.RMIRef);
        } catch (NotBoundException ex) {
            Logger.getLogger(SignInGui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SignInGui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(SignInGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idTB = new javax.swing.JTextField();
        passTB = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        signInBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        newPassTB = new javax.swing.JTextField();
        createBtn = new javax.swing.JButton();
        signInLbl = new javax.swing.JLabel();
        createLbl = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nameTB = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign in");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        idTB.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        getContentPane().add(idTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 57, 309, -1));

        passTB.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        getContentPane().add(passTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 104, 309, -1));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel1.setText("ID");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 56, -1, -1));

        jLabel2.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel2.setText("Password");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 103, -1, -1));

        signInBtn.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        signInBtn.setText("Sign in");
        signInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInBtnActionPerformed(evt);
            }
        });
        getContentPane().add(signInBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 138, 156, 44));

        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel3.setText("Create an account:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 215, -1, -1));

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel4.setText("Password");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 314, -1, -1));

        newPassTB.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        getContentPane().add(newPassTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 315, 306, -1));

        createBtn.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        createBtn.setText("Create");
        createBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBtnActionPerformed(evt);
            }
        });
        getContentPane().add(createBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 355, 162, 46));

        signInLbl.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        getContentPane().add(signInLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 564, 24));

        createLbl.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        getContentPane().add(createLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 400, 560, 23));

        jLabel5.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel5.setText("Name");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 279, -1, -1));

        nameTB.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        nameTB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTBActionPerformed(evt);
            }
        });
        getContentPane().add(nameTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 280, 307, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBtnActionPerformed
        String name=nameTB.getText().trim();
        String pass = newPassTB.getText().trim();
        String id = "";
        try {
            id = serverRef.CreateClient(name,pass);
        } catch (RemoteException ex) {
            Logger.getLogger(SignInGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        createLbl.setText("successfully created an account with id: " + id);
        nameTB.setText("");
        newPassTB.setText("");
    }//GEN-LAST:event_createBtnActionPerformed

    private void signInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInBtnActionPerformed
        nameTB.setText("");
        newPassTB.setText("");
        createLbl.setText("");
        String id = idTB.getText().trim();
        String pass = passTB.getText().trim();
        String res = "";
        try {
            res = serverRef.IdIfValidPass(id, pass);
        } catch (RemoteException ex) {
            Logger.getLogger(SignInGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        res = res.trim();
        if (res.equals("0")) {
            signInLbl.setText("Invalid Sign in !");
            idTB.setText("");
            passTB.setText("");

        } else {
            signInLbl.setText("Valid");
            idTB.setText("");
            passTB.setText("");
            try {
                 whatsapp=new ClientGui(res);
            } catch (NotBoundException ex) {
                Logger.getLogger(SignInGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(SignInGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setVisible(false);
            //
            
        }

    }//GEN-LAST:event_signInBtnActionPerformed

    private void nameTBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTBActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SignInGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignInGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignInGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignInGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignInGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createBtn;
    private javax.swing.JLabel createLbl;
    private javax.swing.JTextField idTB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField nameTB;
    private javax.swing.JTextField newPassTB;
    private javax.swing.JTextField passTB;
    private javax.swing.JButton signInBtn;
    private javax.swing.JLabel signInLbl;
    // End of variables declaration//GEN-END:variables
}
