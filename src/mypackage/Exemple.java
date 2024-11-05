package mypackage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Exemple extends JFrame {
    private Statement st;
    private Conn conn;

    private JPanel General = new JPanel();
    private JPanel Pan0 = new JPanel();
    private DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Nom", "Prenom", "Age"}, 0);
    private JTable Tableau = new JTable(tableModel);
    private JPanel Pan1 = new JPanel();
    private JPanel Pan2 = new JPanel();
    private JPanel Pan3 = new JPanel();
    private JTextField Text1 = new JTextField();
    private JTextField Text2 = new JTextField();
    private JTextField Text3 = new JTextField();
    private JLabel Label1 = new JLabel("Nom");
    private JLabel Label2 = new JLabel("Prenom");
    private JLabel Label3 = new JLabel("Age");
    private JButton Ajouter = new JButton("Ajouter");
    private JButton Supprimer = new JButton("Supprimer");
    private JButton Afficher = new JButton("Afficher");

    public Exemple() {
        conn = new Conn();
        st = conn.getConn();

        setTitle("Exemple Global");
        setSize(530, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(General);
        General.setLayout(new BorderLayout());
        General.add(Pan0, BorderLayout.CENTER);
        Pan0.setLayout(new FlowLayout());
        Tableau.setFillsViewportHeight(true);
        Dimension preferredSize = new Dimension(500, 150);
        Tableau.setPreferredScrollableViewportSize(preferredSize);
        Pan0.add(new JScrollPane(Tableau));
        General.add(Pan1, BorderLayout.NORTH);
        Pan1.setLayout(new GridLayout(2, 1));
        Pan1.add(Pan2);
        Pan1.add(Pan3);
        Pan2.setLayout(new GridLayout(3, 2));
        Pan2.add(Label1);
        Pan2.add(Text1);
        Pan2.add(Label2);
        Pan2.add(Text2);
        Pan2.add(Label3);
        Pan2.add(Text3);
        Pan3.setLayout(new FlowLayout());
        Pan3.add(Ajouter);
        Pan3.add(Supprimer);
        Pan3.add(Afficher);
        
        Afficher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherPersonnes();
            }
        });

        Ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterPersonne();
            }
        });

        Supprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerPersonne();
            }
        });
        setVisible(true);
    }
    
    private void afficherPersonnes() {
        String SQLAfficher = "SELECT * FROM Personne";
        try {
            ResultSet rs = st.executeQuery(SQLAfficher);
            
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                String nom = rs.getString("Nom");
                String prenom = rs.getString("Prenom");
                int age = rs.getInt("Age");
                tableModel.addRow(new Object[]{nom, prenom, age});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des données !");
            e.printStackTrace();
        }
    }

    private void ajouterPersonne() {
        String SQLAjout = "INSERT INTO Personne (Nom, Prenom, Age) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.getConnection().prepareStatement(SQLAjout)) {

            pstmt.setString(1, Text1.getText());
            pstmt.setString(2, Text2.getText());
            pstmt.setInt(3, Integer.parseInt(Text3.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Insertion réussie !");
            
            Text1.setText("");
            Text2.setText("");
            Text3.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'insertion !");
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un âge valide !");
        }
    }

    private void supprimerPersonne() {
        String SQLSupprimer = "DELETE FROM Personne WHERE Nom = ?";
        try (PreparedStatement pstmt = conn.getConnection().prepareStatement(SQLSupprimer)) {

            pstmt.setString(1, Text1.getText());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Suppression réussie !");
            } else {
                JOptionPane.showMessageDialog(this, "Aucune personne trouvée avec ce nom.");
            }

            Text1.setText("");
            Text2.setText("");
            Text3.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression !");
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Exemple();
    }
}
