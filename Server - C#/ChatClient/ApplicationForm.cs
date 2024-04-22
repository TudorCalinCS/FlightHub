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
using chat.network.client;
using FlightHubC_.Domain;

namespace FlightHubC_
{
    [Serializable]
    public partial class ApplicationForm : Form
    {
        private ChatClientCtrl ctrl;

        public ApplicationForm(ChatClientCtrl ctrl)
        {
            InitializeComponent();
            this.ctrl = ctrl;
            ctrl.updateEvent += Ctrl_TicketBought;
        }
        private void Ctrl_TicketBought(object sender, EventArgs e)
        {
            try { 
            this.BeginInvoke(new Action(() => { PopulateDataGridView(); }));
            }
            catch(Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }
        private void ApplicationForm_Load(object sender, EventArgs e)
        {   
            usernameLabel.Text=ctrl.currentUser.Username;
            InitializeDataGridView();
            PopulateDataGridView();
            InitializeDataGridView2();

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }
        private void InitializeDataGridView()
        {
            // Set DataGridView properties
            dataGridView1.AutoGenerateColumns = false;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;

            // Add columns to DataGridView
            dataGridView1.Columns.Add("Destination", "Destination");
            dataGridView1.Columns.Add("DateTime", "DateTime");
            dataGridView1.Columns.Add("AvailableSeats", "AvailableSeats");
            dataGridView1.Columns.Add("Airport", "Airport");
        }

        private void PopulateDataGridView()
        {
            dataGridView1.Rows.Clear();
            
            List<Flight> flights = ctrl.getFlights();
            // Populate DataGridView with flights
            foreach (Flight flight in flights)
            {
                dataGridView1.Rows.Add(flight.Destination, flight.DateTime,flight.AvailableSeats,flight.Airport);
            }
        }

        private void searchButton_Click(object sender, EventArgs e)
        {
            DateTime dateTime = dateTimePicker1.Value;
            String destination = textBox1.Text;
            if(destination.Length ==0) {
                MessageBox.Show("Destination is empty!");
                return;
            }
            // List<Flight> flights= service.SearchFlight(destination,dateTime);
            List<Flight> flights = ctrl.SearchFlights(destination,dateTime);
            PopulateDataGridView2(flights);
        }
        private void InitializeDataGridView2()
        {
            // Set DataGridView properties
            dataGridView2.AutoGenerateColumns = false;
            dataGridView2.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;

            // Add columns to DataGridView
            dataGridView2.Columns.Add("Destination", "Destination");
            dataGridView2.Columns.Add("DateTime", "DateTime");
            dataGridView2.Columns.Add("AvailableSeats", "AvailableSeats");
            dataGridView2.Columns.Add("Airport", "Airport");
        }

        private void PopulateDataGridView2(List<Flight> flights)
        {       
            dataGridView2.Rows.Clear();            

            // Populate DataGridView with flights
            foreach (Flight flight in flights)
            {
                dataGridView2.Rows.Add(flight.Destination, flight.DateTime, flight.AvailableSeats, flight.Airport);
            }
        }

        private void buyButton_Click(object sender, EventArgs e)
        {
            if (dataGridView2.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedRow = dataGridView2.SelectedRows[0];

                string destination = selectedRow.Cells["Destination"].Value.ToString();
                DateTime dateTime = (DateTime)selectedRow.Cells["DateTime"].Value;
                int availableSeats = Convert.ToInt32(selectedRow.Cells["AvailableSeats"].Value);
                string airport = selectedRow.Cells["Airport"].Value.ToString();

                 Flight flight=ctrl.findByDetails(destination,dateTime,availableSeats,airport);


                TicketForm ticketForm = new TicketForm();
                ticketForm.ShowDialog();

                string clientsname = ticketForm.ClientsName;
                string clientsaddress = ticketForm.ClientsAddress;
                string touristsname = ticketForm.TouristsName;
                int numberOfSeats = ticketForm.NumberOfSeats;


                bool buyTicket = ctrl.BuyTicket(clientsname, clientsaddress, touristsname, numberOfSeats, flight);

                if(buyTicket)
                {
                    MessageBox.Show("Ticket bought!");
                    dataGridView2.Rows.Clear();
                    //PopulateDataGridView();
                }
            }
            else
            {
                MessageBox.Show("Please select a flight from the list.");
            }
            
        }

        private void logoutButton_Click(object sender, EventArgs e)
        {
            ctrl.updateEvent -= Ctrl_TicketBought;
            Application.Exit();
        }

        private void ApplicationForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            ctrl.updateEvent -= Ctrl_TicketBought;
            Application.Exit();
        }
    }
}
