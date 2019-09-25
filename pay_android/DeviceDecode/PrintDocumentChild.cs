using System;
using System.Collections.Generic;
using System.Drawing.Printing;
using System.Linq;
using System.Text;

namespace DeviceDecode
{
    class PrintDocumentChild: PrintDocument
    {
        public OrderInfo orderInfo { set; get; }
        public Print print { set; get; }

        public OrderStatisticsInfo orderStatis { set; get; }

    }
}
