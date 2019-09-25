using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DeviceDecode
{
    class Print
    {
        public int id { set; get; }
        public int type { set; get; }
        public string name { set; get; }
        public string port { set; get; }
        public int flag { set; get; }
        public int isPay { set; get; }
        public int isFull { set; get; }
        public int autoPrint { set; get; }
        public Cp cp2 { set; get; }
        public List<Cp> cps { set; get; }
        public List<Cp> cpsRefund { set; get; }
    }
}
