using System;
using System.Collections;
using System.Collections.Generic;
using System.Windows.Forms;
using chat.services;
using chat.network.server;
using Hashtable=System.Collections.Hashtable;
using FlightHubC_;

namespace chat.client
{
    static class StartChatClient
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            
           
            //IChatServer server=new ChatServerMock();   
            //FOLOSITI FISIERE DE CONFIGURARE PENTRU A OBTINE IP SI PORT
            //exemplu in GTKClient
            IChatServices server = new ChatServerProxy("127.0.0.1", 55556);
            ChatClientCtrl ctrl=new ChatClientCtrl(server);
            //LoginWindow win=new LoginWindow(ctrl);
            LoginForm win = new LoginForm(ctrl);
            Application.Run(win);
        }
    }
}
