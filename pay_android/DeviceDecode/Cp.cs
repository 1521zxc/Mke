using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DeviceDecode
{
    class Cp
    {
        public int id { set; get; }
        public string name { set; get; }
        public double price { set; get; }
        public int count { set; get; }
        public string sellStatus { set; get; }
        public string remark { set; get; }
        public double memberPrice { set; get; }
        public int ODID { set; get; }
        public int printID { set; get; }
        public int flag { set; get; }
        public List<CpAttribution> attrs { set; get; }
        public string subItems { set; get; }
        public List<Cp> cpChild { set; get; }
        public string removeId { set; get; }
        public int isSub { set; get; }
        public int isPrint { set; get; }
        public int isRefundPrint { set; get; }
    }
}
