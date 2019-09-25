using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DeviceDecode
{
    class OrderStatisticsInfo
    {
        public List<StatisticsItem> statisticsItems { set; get; }
        public string type { set; get; }
        public string startTime { set; get; }
        public string endTime { set; get; }
        public int totalNum { set; get; }
        public int count { set; get; }
        public int totalDesk { set; get; }
        public double totalSum { set; get; }
        public double totalMoney { set; get; }
        public double yhMoney { set; get; } //优惠
    }
}
