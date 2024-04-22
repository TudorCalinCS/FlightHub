using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace chat.client
{
    public enum ChatUserEvent
    {
         BoughtTicket
    } ;
    public class ChatUserEventArgs : EventArgs
    {
        private readonly ChatUserEvent userEvent;

        public ChatUserEventArgs(ChatUserEvent userEvent)
        {
            this.userEvent = userEvent;
        }

        public ChatUserEvent UserEventType
        {
            get { return userEvent; }
        }

       
    }
}
