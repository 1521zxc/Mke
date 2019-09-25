using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace DeviceDecode
{
    class PanelChild: Panel
    {
        public int ID { set; get; }
        public long createTime { set; get; }
        public int payType { set; get; }
        public string orderNO { set; get; }
        public int deskState { set; get; }
        public string deskNO { set; get; }
        public string deskName { set; get; }
        public string ordinal { set; get; }
        public string payNO { set; get; }
        public double primeMoney { set; get; }
        public double saleMoney { set; get; }
        public double paidMoney { set; get; }
        public double memberMoney { set; get; }
        public int serviceStatus { set; get; }
        public int payStatus { set; get; }
        public int location { set; get; }

        public CheckBoxChild ck { set; get; }
    }
}
