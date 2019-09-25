using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DeviceDecode
{
   public class DeskInfo
    {
        public int ID { set; get; }
        public string deskNO { set; get; }
        public string deskName { set; get; }
        public string deskStatus { set; get; }
        public int orderID { set; get; }
        public string orderNO { set; get; }
        public int totalNum { set; get; }
        public int serviceStatus { set; get; }
        public int payStatus { set; get; }
        public double saleMoney { set; get; }
    }
}
