using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace DeviceDecode
{
    class ButtonChild : Button
    {
        public string orderNo { set; get; }
        public int orderId { set; get; }
        public string payNo { set; get; }
        public double handlePrice { set; get; }
        public double reducedPrice { set; get; }
        public double yhPrice { set; get; }
        public double realityPrice { set; get; }
    }
}
