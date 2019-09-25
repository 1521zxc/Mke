using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Printing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace DeviceDecode
{
    public partial class statement : Form
    {
        int type=0;
        bool beginMove = false;//初始化鼠标位置  
        int currentXPosition;
        int currentYPosition;
        public statement()
        {
            InitializeComponent();

            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 4 }, { "muuid", Login.userInfo.muuid } };
            JObject json3 = new JObject { { "cataIndex", 0 } };

            json2.Add("data", json3.ToString());
            string result = string.Empty;
            result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
            if (result != string.Empty)
            {

                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                if (jo["errorCode"].ToString().Equals("0"))
                {
                    List<Menu> menus = new List<Menu>();
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                    JArray menuArray = JArray.Parse(jsonObect["items"].ToString());
                    Menu mm = new Menu();
                    mm.id = -1;
                    mm.name = "全部分类";
                    menus.Add(mm);
                    for (int i = 0; i < menuArray.Count; i++)
                    {
                        Menu m = new Menu();
                        m.id = int.Parse(menuArray[i]["ID"].ToString());
                        m.name = menuArray[i]["name"].ToString();
                        menus.Add(m);

                    }

                    this.comboBox1.DataSource = menus;
                    this.comboBox1.ValueMember = "id";
                    this.comboBox1.DisplayMember = "name";
                }
            }
        }
        private void button3_Click(object sender, EventArgs e)
        {
            this.Close();
            this.Dispose();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.WindowState = FormWindowState.Minimized;
        }

        private void panel1_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                beginMove = true;
                currentXPosition = MousePosition.X;//鼠标的x坐标为当前窗体左上角x坐标  
                currentYPosition = MousePosition.Y;//鼠标的y坐标为当前窗体左上角y坐标  
            }
        }

        private void panel1_MouseMove(object sender, MouseEventArgs e)
        {
            if (beginMove)
            {
                this.Left += MousePosition.X - currentXPosition;//根据鼠标x坐标确定窗体的左边坐标x  
                this.Top += MousePosition.Y - currentYPosition;//根据鼠标的y坐标窗体的顶部，即Y坐标  
                currentXPosition = MousePosition.X;
                currentYPosition = MousePosition.Y;
            }
        }

        private void panel1_MouseUp(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                currentXPosition = 0; //设置初始状态  
                currentYPosition = 0;
                beginMove = false;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try { 
            long start = ConvertDateTimeToInt(this.dateTimePicker2.Value);
            long end = ConvertDateTimeToInt(this.dateTimePicker1.Value);
            if (type == 0)
            {
                MessageBox.Show("请选择报表类型");
            }
            else if(type==1)
            {
                
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 95 }, { "token", Login.userInfo.token } };
                JObject json3 = new JObject { { "fromTime", start }, { "toTime", end } };

                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                Console.WriteLine(result);
                printStatement(result, "财务报表", this.dateTimePicker2.Value.ToString(), this.dateTimePicker1.Value.ToString());
              
                
            }
            else if (type == 2)
            {
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 94 }, { "token", Login.userInfo.token } };
                JObject json3 = null;
                if (int.Parse(this.comboBox1.SelectedValue.ToString()) == -1)
                {
                    json3 = new JObject { { "fromTime", start }, { "toTime", end } };
                }
                else
                {
                    json3 = new JObject { { "cataID", int.Parse(this.comboBox1.SelectedValue.ToString()) }, { "fromTime", start }, { "toTime", end } };
                }                           

                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                Console.WriteLine(result);
                Console.WriteLine("this.comboBox1.SelectedText.ToString()"+this.comboBox1.SelectedText.ToString());
                printStatement(result,this.comboBox1.Text,this.dateTimePicker2.Value.ToString(), this.dateTimePicker1.Value.ToString());

            }
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
    

        private void panel4_Click(object sender, EventArgs e)
        {
            type = 2;
            this.panel3.BackgroundImage = Properties.Resources.financial_statements;
            this.panel4.BackgroundImage = Properties.Resources.class_statements_click;
        }

        private void panel3_Click(object sender, EventArgs e)
        {
            type = 1;
            this.panel3.BackgroundImage = Properties.Resources.financial_statements_click;
            this.panel4.BackgroundImage = Properties.Resources.class_statements;
        }
        public static long ConvertDateTimeToInt(System.DateTime time)
        {
            System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1, 0, 0, 0, 0));
            long t = (time.Ticks - startTime.Ticks) / 10000;   //除10000调整为13位      
            return t;
        }

        public static void printStatement(string result,string type,string startTime,string endTime)
        {
            if (result != string.Empty)
            {
                Console.WriteLine("print" + result);
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                if (jo["errorCode"].ToString().Equals("0"))
                {
                  
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                    if (jsonObect["count"] != null && int.Parse(jsonObect["count"].ToString()) > 0)
                    {
                        if (jsonObect["printers"] == null)
                        {
                            MessageBox.Show("未设置打印机");
                        }
                        else
                        {
                            OrderStatisticsInfo orderSta = new OrderStatisticsInfo();
                            orderSta.type = type;
                            orderSta.startTime = startTime;
                            orderSta.endTime = endTime;
                            if (jsonObect["count"] != null)
                            {
                                orderSta.count = int.Parse(jsonObect["count"].ToString());
                            }
                            if (jsonObect["totalNum"] != null)
                            {
                                orderSta.totalNum = int.Parse(jsonObect["totalNum"].ToString());
                            }
                            if (jsonObect["totalDesk"] != null)
                            {
                                orderSta.totalDesk = int.Parse(jsonObect["totalDesk"].ToString());
                            }
                            if (jsonObect["totalSum"] != null)
                            {
                                orderSta.totalSum = double.Parse(jsonObect["totalSum"].ToString());//收金额
                            }
                            if (jsonObect["totalMoney"] != null)
                            {
                                orderSta.totalMoney = double.Parse(jsonObect["totalMoney"].ToString());//应收
                            }
                            if (jsonObect["offMoney"] != null)
                            {
                                orderSta.yhMoney = double.Parse(jsonObect["offMoney"].ToString());//优惠
                            }

                            if (jsonObect["items"] != null)
                            {
                                List<StatisticsItem> statiss = new List<StatisticsItem>();
                                JArray jarry = JArray.Parse(jsonObect["items"].ToString());
                                for (int i = 0; i < jarry.Count; i++)
                                {
                                    StatisticsItem statis = new StatisticsItem();
                                    if (jarry[i]["name"] != null)
                                    {
                                        statis.name = jarry[i]["name"].ToString();
                                    }
                                    if (jarry[i]["count"] != null)
                                    {
                                        statis.count = int.Parse(jarry[i]["count"].ToString());
                                    }
                                    if (jarry[i]["sum"] != null)
                                    {
                                        statis.money = double.Parse(jarry[i]["sum"].ToString());
                                    }
                                    statiss.Add(statis);
                                }
                                orderSta.statisticsItems = statiss;
                            }
                            List<Print> prints = new List<Print>();
                            if (jsonObect["printers"] != null)
                            {
                                JArray printArray = JArray.Parse(jsonObect["printers"].ToString());
                                for (int i = 0; i < printArray.Count; i++)
                                {
                                    Print print = new Print();
                                    print.id = int.Parse(printArray[i]["ID"].ToString());
                                    print.type = int.Parse(printArray[i]["type"].ToString());
                                    print.name = printArray[i]["name"].ToString();
                                    print.port = printArray[i]["port"].ToString();

                                    print.flag = int.Parse(printArray[i]["flag"].ToString());
                                    if (printArray[i]["isPay"] != null)
                                    {
                                        print.isPay = int.Parse(printArray[i]["isPay"].ToString());
                                    }
                                    else
                                    {
                                        print.isPay = 0;
                                    }

                                    prints.Add(print);
                                }

                            }
                            else
                            {
                                MessageBox.Show("打印机未设置或者打印机分类不全");
                            }

                            for (int i = 0; i < prints.Count; i++)
                            {
                                if (prints[i].isPay != 0)
                                {
                                    PrintPreviewDialog ppd = new PrintPreviewDialog();

                                    PrintDocumentChild pd = new PrintDocumentChild();
                                    pd.orderStatis = orderSta;

                                    PrintDialog MyDlg = new PrintDialog();

                                    //设置边距
                                    Margins margin = new Margins(20, 20, 20, 20);
                                    pd.DefaultPageSettings.Margins = margin;


                                    ////纸张设置默认
                                    PaperSize pageSize = new PaperSize("First custom size", getYc(80), 1000);
                                    pd.DefaultPageSettings.PaperSize = pageSize;

                                    //打印事件设置            
                                    pd.PrinterSettings.PrinterName = prints[i].name;
                                    pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                    MyDlg.Document = pd;
                                    ppd.Document = pd;
                                    (ppd as Form).WindowState = FormWindowState.Maximized;
                                    if (ppd.ShowDialog() == DialogResult.OK)
                                    {
                                        pd.Print();
                                    }
                                }
                            }


                        }

                    }
                    else
                    {
                        MessageBox.Show("暂无销售记录");

                    }

                }
                else
                {
                    MessageBox.Show(jo["message"].ToString());
                }

            }
        }
        private static int getYc(double cm)

        {

            return (int)(cm / 25.4) * 100;

        }

        public static void GetPrintStr(object sender, PrintPageEventArgs e)

        {
            try { 
            int top = 2;
            int left = 6;
            PrintDocumentChild printDocumentChild = (PrintDocumentChild)sender;
            Panel p = new Panel();
            Label shopName = new Label();
            shopName.Text = Login.userInfo.brandName;
            shopName.Left = left;
            shopName.Font = new Font("微软雅黑", 10);
            shopName.ForeColor = Color.Gray;
            shopName.Top = 10;

            Label type = new Label();
            type.Text = "报表类别: " + printDocumentChild.orderStatis.type;
            type.Left = left;
            type.Font = new Font("微软雅黑", 10);
            type.ForeColor = Color.Gray;
            type.Top = top + type.Height + shopName.Top - 3;

            Label u = new Label();
            u.Text = "--------------------------------------------";
            u.Left = left;
            u.Font = new Font("微软雅黑", 10);
            u.ForeColor = Color.Gray;
            u.Top = top + u.Height + type.Top - 3;

            Label dNumber = new Label();
            dNumber.Text = "报表打印时间: "+DateTime.Now.ToShortDateString();
            dNumber.Left = left;
            dNumber.Font = new Font("微软雅黑", 10);
            dNumber.ForeColor = Color.Gray;
            dNumber.Top = top + dNumber.Height + u.Top - 3;

            Label odinal = new Label();
            odinal.Text = "报表起止日期范围";
            odinal.Left = left;
            odinal.Font = new Font("微软雅黑", 10);
            odinal.ForeColor = Color.Gray;
            odinal.Top = top + odinal.Height + dNumber.Top - 3;

            Label startTime = new Label();
            startTime.Text = printDocumentChild.orderStatis.startTime;
            startTime.Left = left;
            startTime.Font = new Font("微软雅黑", 10);
            startTime.ForeColor = Color.Gray;
            startTime.Top = top + startTime.Height + odinal.Top - 3;

            Label endTime = new Label();
            endTime.Text = printDocumentChild.orderStatis.endTime;
            endTime.Left = left;
            endTime.Font = new Font("微软雅黑", 10);
            endTime.ForeColor = Color.Gray;
            endTime.Top = top + endTime.Height + startTime.Top - 3;


          //  System.DateTime orderTIME = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
           // DateTime dt = orderTIME.AddMilliseconds(printDocumentChild.orderInfo.createTime);
          //  Label orderDate = new Label();
          //  orderDate.Text = "下单时间: " + dt.ToString("yyyy/MM/dd HH:mm:ss"); ;
          //  orderDate.Left = left;
          //  orderDate.Font = new Font("微软雅黑", 10);
          //  orderDate.ForeColor = Color.Gray;
          //  orderDate.Top = top + orderDate.Height + endTime.Top - 3;

            Label u2 = new Label();
            u2.Text = "--------------------------------------------";
            u2.Left = left;
            u2.Font = new Font("微软雅黑", 10);
            u2.ForeColor = Color.Gray;
            u2.Top = top + u2.Height + endTime.Top - 3;

            Label cpName = new Label();
            cpName.Text = "名称";
            cpName.Left = left;
            cpName.Font = new Font("微软雅黑", 10);
            cpName.ForeColor = Color.Gray;
            cpName.Top = top + cpName.Height + u2.Top;

            Label cpCount = new Label();
            cpCount.Text = "数量";
            cpCount.Left = left + 150;
            cpCount.Font = new Font("微软雅黑", 10);
            cpCount.ForeColor = Color.Gray;
            cpCount.Top = top + cpName.Height + u2.Top;

            Label cpPrice = new Label();
            cpPrice.Text = "金额";
            cpPrice.Left = left + 210;
            cpPrice.Font = new Font("微软雅黑", 10);
            cpPrice.ForeColor = Color.Gray;
            cpPrice.Top = top + cpName.Height + u2.Top;
            p.Controls.Add(cpName);
            p.Controls.Add(cpCount);
            p.Controls.Add(cpPrice);
            int t = 0;
            int l = cpPrice.Top;
            for (int i = 0; i < printDocumentChild.orderStatis.statisticsItems.Count; i++)

            {
                Label name = new Label();
                name.Left = left;
                name.ForeColor = Color.Gray;

                Label count = new Label();
                count.Left = left + 150;
                count.ForeColor = Color.Gray;

                Label price = new Label();
                price.Left = left + 210;
                price.ForeColor = Color.Gray;
                name.Top = top + name.Height + l + i * 20;
                count.Top = top + count.Height + l + i * 20;
                price.Top = top + price.Height + l + i * 20;

                t = price.Top;
              
                name.Text = printDocumentChild.orderStatis.statisticsItems[i].name;
                count.Text = printDocumentChild.orderStatis.statisticsItems[i].count.ToString();
                price.Text = printDocumentChild.orderStatis.statisticsItems[i].money.ToString();
                price.Font = new Font("微软雅黑", 10);
                name.Font = new Font("微软雅黑", 10);
                count.Font = new Font("微软雅黑", 10);
              
                p.Controls.Add(name);
                p.Controls.Add(count);
                p.Controls.Add(price);

            }

            Label u3 = new Label();
            u3.Text = "--------------------------------------------";
            u3.Left = left;
            u3.Font = new Font("微软雅黑", 10);
            u3.ForeColor = Color.Gray;
            u3.Top = top + u3.Height + t;
            p.Controls.Add(u3);

            Label remark = new Label();
            remark.Text = "应收总额: ￥" + printDocumentChild.orderStatis.totalMoney;
            remark.Left = left;
            remark.Font = new Font("微软雅黑", 10);
            remark.ForeColor = Color.Gray;
            remark.Top = top + remark.Height + u3.Top;
            p.Controls.Add(remark);

            Label yh = new Label();
            yh.Text = "优惠总额: ￥" + printDocumentChild.orderStatis.yhMoney;
            yh.Left = left+150;
            yh.Font = new Font("微软雅黑", 10);
            yh.ForeColor = Color.Gray;
            yh.Top = top + remark.Height + u3.Top;
            p.Controls.Add(yh);

            Label primeMoney = new Label();
            primeMoney.Text = "实收总额: ￥" + printDocumentChild.orderStatis.totalSum;
            primeMoney.Left = left;
            primeMoney.Font = new Font("微软雅黑", 10);
            primeMoney.ForeColor = Color.Gray;
            primeMoney.Top = top + primeMoney.Height + remark.Top;
            p.Controls.Add(primeMoney);

            Label peoCount = new Label();
            peoCount.Text = "顾客总数: " + printDocumentChild.orderStatis.totalNum;
            peoCount.Left = left;
            peoCount.Font = new Font("微软雅黑", 10);
            peoCount.ForeColor = Color.Gray;
            peoCount.Top = top + peoCount.Height + primeMoney.Top;
            p.Controls.Add(peoCount);

            Label deskCount = new Label();
            deskCount.Text = "营业笔数: " + printDocumentChild.orderStatis.totalDesk;
            deskCount.Left = left;
            deskCount.Font = new Font("微软雅黑", 10);
            deskCount.ForeColor = Color.Gray;
            deskCount.Top = top + deskCount.Height + peoCount.Top;
            p.Controls.Add(deskCount);

            p.Controls.Add(shopName);
            p.Controls.Add(type);
            p.Controls.Add(u);
            p.Controls.Add(dNumber);
            p.Controls.Add(odinal);
            p.Controls.Add(startTime);
            p.Controls.Add(endTime);
            p.Controls.Add(u2);


            foreach (Control c in p.Controls)
            {
                if (c.GetType() == typeof(Label))
                {
                    e.Graphics.DrawString(c.Text, c.Font, new SolidBrush(c.ForeColor), c.Location);
                }

            }
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }

    }
}
