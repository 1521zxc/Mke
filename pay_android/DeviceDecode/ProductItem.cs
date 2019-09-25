using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DeviceDecode
{
    class ProductItem
    {
        public int ID { set; get; }
        public int count { set; get; }

        public List<ProdAttrItem> attrs { set; get; }
    }
}
