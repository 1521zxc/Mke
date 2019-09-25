using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DeviceDecode
{
  public  class OrderInfo
    {
        public int ID { set; get; }
        public int ordinal { set; get; }
        public long createTime { set; get; }
        public long payTime { set; get; }
        public int payType { set; get; }
        public string orderNO { set; get; }
        public string deskNO { set; get; }

        public string peocount { set; get; }
        public string deskName { set; get; }
        public string remark { set; get; }
        public string payURL { set; get; }
        public string payNO { set; get; }
        public double primeMoney { set; get; }
        public double balanceMoney { set; get; }  //余额
        public double cashMoney { set; get; }  //现金
        public double saleMoney { set; get; }
        public double refundMoney { set; get; }
        public string yh { set; get; }//优惠
        public double paidMoney { set; get; }
        public double memberMoney { set; get; }
        public int serviceStatus { set; get; }
        public int payStatus { set; get; }
    }
}
