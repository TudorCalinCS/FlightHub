using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using chat.client;
using FlightHubC_.Domain;

namespace FlightHubC_
{
    public partial class LoginForm : Form
    {
        //private Service service;
        private ChatClientCtrl ctrl;

        public LoginForm(ChatClientCtrl ctrl)
        {
            InitializeComponent();
            //this.service = service;
            this.ctrl = ctrl;
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void LoginButton_Click(object sender, EventArgs e)
        {
            String username = textBox1.Text;
            String password = textBox2.Text;

            try {
                ctrl.login(username, password);
                ApplicationForm applicationForm = new ApplicationForm(ctrl);
                this.Hide();
                applicationForm.Show();
            }
            catch (Exception ex)
            {
                MessageBox.Show(this, "Login Error " + ex.Message/*+ex.StackTrace*/, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
            
        }

        private void LoginForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            //Application.Exit();
        }
    }
}
