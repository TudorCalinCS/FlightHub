using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace chat.services
{
    public class TException : Exception
    {
        public TException():base() { }

        public TException(String msg) : base(msg) { }

        public TException(String msg, Exception ex) : base(msg, ex) { }

    }
}
