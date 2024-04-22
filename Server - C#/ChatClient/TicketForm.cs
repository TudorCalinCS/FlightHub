using System;
using System.Windows.Forms;

namespace chat.client
{
    public partial class TicketForm : Form
    {
        public string ClientsName { get; set; }
        public string ClientsAddress { get; set; }
        public string TouristsName { get; set; }
        public int NumberOfSeats { get; set; }
        public TicketForm()
        {   
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if(textBox1.Text.Length == 0 ||textBox2.Text.Length==0||textBox3.Text.Length==0||textBox4.Text.Length==0) {
                MessageBox.Show("Incomplete data!");
                return;
            }
            ClientsName = textBox1.Text;
            ClientsAddress = textBox2.Text;
            TouristsName = textBox3.Text;
            NumberOfSeats = int.Parse(textBox4.Text);

            this.Close();
        }

        private void TicketForm_Load(object sender, EventArgs e)
        {

        }

        private void TicketForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            //Application.Exit(); 
        }
    }
}
