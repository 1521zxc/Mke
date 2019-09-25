using Microsoft.VisualBasic;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Printing;
using System.IO;
using System.Linq;
using System.Net;
using System.Reflection;
using System.Security.Permissions;
using System.Text;
using System.Windows.Forms;
using WebSocketSharp;

namespace DeviceDecode
{

    public partial class MainForm : Form
    {
        public static string orderNo;
        public static double payPrice;
        public static int orderId;
        public static string payNo;
        public static int sellState;
        private static PanelChild[] removeArray = new PanelChild[1];
        private static PanelChild[] panelArray = new PanelChild[1];
        private static PanelChild[] panelArrayDesk = new PanelChild[1];
        public static OrderInfo orderInfo = new OrderInfo();

        public static string testurl = "https://api.jqepay.com/default";
       // public static string testurl = "http://192.168.1.115:3000/jqepay";
        public static order o = new order();
        public static pay p = new pay();
        public static desk d = new desk();
        bool beginMove = false;//初始化鼠标位置  
        int currentXPosition;
        int currentYPosition;

        public MainForm()
        {
            InitializeComponent();
           
            using (var ws = new WebSocket("wss://www.jqepay.com/wssmsg/" + Login.userInfo.id))
            {
                ws.OnMessage += (sender, e) =>
                printOrderDetail(e.Data);
                // MessageBox.Show(e.Data);
                ws.Connect();
                ws.OnClose += (sender, e) =>
                {
                    ws.Connect();
                };

            }

            isClose(1);
           
            o = new order();
            p = new pay();

        }

        public void order_Click(object sender, EventArgs e)
        {
            try {
                orderNo = null;
                o.type = -1;
                o.button1.BackColor = Color.FromArgb(255, 221, 221, 221);
                o.button1.Text = "· 订单";
                o.button2.BackColor = Color.Transparent;
                o.button2.Text = "预约";
                o.button5.BackColor = Color.Transparent;
                o.button5.Text = "预定";
                this.order.BackgroundImage = Properties.Resources.order;
                this.payorder.BackgroundImage = Properties.Resources.payleave;
                this.desk.BackgroundImage = Properties.Resources.deskleave;
                this.panelAll.Controls.Clear();
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 90 }, { "token", Login.userInfo.token } };
                JObject json3 = new JObject { { "totalCount", 1 }, { "pageSize",9 } };

                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                string result = string.Empty;
                result = HttpPostData(testurl.ToString(), json2.ToString());
                Console.WriteLine(result);
                o.SetSearchDefaultText();
                getOrderList(result);
                o.pageIndex.Text = "1";
            }
            catch (Exception q)
            { MessageBox.Show(q.Message); }

        }
        public void getOrderList(string result)
        {
            if (result != string.Empty)
            {
                o.orderPanel.Controls.Clear();
                o.detailPanel.Controls.Clear();
                PictureBox picture = new PictureBox();
                picture.Size = new Size(183, 305);
                picture.Location = new Point(73, 84);
                picture.BackgroundImage = Properties.Resources.noDeatail;
                o.detailPanel.Controls.Add(picture);
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                if (jo["errorCode"].ToString().Equals("0"))
                {
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());


                    if (int.Parse(jsonObect["count"].ToString()) > 0)
                    {
                        o.count.Text = jsonObect["totalCount"].ToString();
                        o.pageIndex.Text = "1";
                        int page = int.Parse(jsonObect["totalCount"].ToString()) / 9;
                        double y = double.Parse(jsonObect["totalCount"].ToString()) % 9;
                        if (y > 0)
                        {
                            page += 1;
                        }

                        o.page.Text = page.ToString();

                        JArray jarry = JArray.Parse(jsonObect["items"].ToString());

                        o.Show();

                        this.panelAll.Controls.Add(o);

                        int top = 2;
                        int left = 6;
                        int j = 0;
                        int k = 0;
                        for (int i = 0; i < jarry.Count; i++)
                        {
                            Label l1 = new Label();
                            l1.BackColor = Color.Transparent;
                            if (jarry[i]["deskName"] != null)
                            {
                                l1.Name = "桌台：" + jarry[i]["deskName"].ToString();
                                l1.AutoSize = true;
                                l1.Text = l1.Name;
                                l1.Left = left;
                                l1.Top = top + l1.Height;
                                l1.Font = new Font("微软雅黑", 14);
                                l1.ForeColor = Color.FromArgb(255, 51, 51, 51);
                            }
                            else
                            {
                                l1.Name = "订单序号：" + jarry[i]["ordinal"].ToString();
                                l1.Text = l1.Name;
                                l1.AutoSize = true;
                                l1.Left = left;
                                l1.Top = top + l1.Height;
                                l1.Font = new Font("微软雅黑", 14);
                                l1.ForeColor = Color.FromArgb(255, 51, 51, 51);
                            }


                            Label lblTitle = new Label();
                            lblTitle.BackColor = Color.Transparent;
                            lblTitle.AutoSize = true;
                            if (jarry[i]["createTime"] == null)
                            {
                                //MessageBox.Show("服务器异常：时间为空");
                                lblTitle.Name = "";
                            }
                            else
                            {
                                System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                                DateTime dt = startTime.AddMilliseconds(long.Parse(jarry[i]["createTime"].ToString()));
                                lblTitle.Name = "时间：" + dt.ToString("yyyy/MM/dd HH:mm:ss");

                            }


                            lblTitle.Text = lblTitle.Name;
                            lblTitle.Left = left;
                            lblTitle.Font = new Font("微软雅黑", 10);
                            lblTitle.ForeColor = Color.FromArgb(255, 51, 51, 51);
                            lblTitle.Top = 10 + lblTitle.Height + l1.Top+5;                           

                            Label lblTitle3 = new Label();
                            lblTitle3.BackColor = Color.Transparent;
                            lblTitle3.AutoSize = true;
                            if (jarry[i]["primeMoney"] == null)
                            {
                               // MessageBox.Show("服务器异常：原始金额为空");
                                lblTitle3.Name = "金额";
                            }
                            else
                            {
                                lblTitle3.Name = "金额：" + jarry[i]["primeMoney"].ToString();
                            }

                            lblTitle3.Font = new Font("微软雅黑", 10);
                            lblTitle3.ForeColor = Color.FromArgb(255, 51, 51, 51);
                            lblTitle3.Text = lblTitle3.Name;
                            lblTitle3.Left = left;
                            lblTitle3.Top = top + lblTitle3.Height + lblTitle.Top+5;


                            Button remove = new Button();
                            remove.FlatStyle = FlatStyle.Flat;
                            remove.FlatAppearance.BorderSize = 0;
                            remove.Size = new Size(15, 15);

                            //remove.Text = "✘";
                            remove.Font = new Font("宋体", 14);
                            remove.Location = new Point(174, 0);


                            ButtonChild pay = new ButtonChild();
                            pay.FlatStyle = FlatStyle.Flat;
                            pay.FlatAppearance.BorderSize = 0;
                            pay.Size = new Size(50, 20);

                            if (jarry[i]["primeMoney"] != null)
                            {
                                pay.handlePrice = double.Parse(jarry[i]["primeMoney"].ToString());
                            }
                            if (jarry[i]["vipMoney"] != null)
                            {
                                pay.reducedPrice = double.Parse(jarry[i]["vipMoney"].ToString());
                            }
                            if (jarry[i]["orderNO"] != null)
                            {
                                pay.orderNo = jarry[i]["orderNO"].ToString();
                            }



                            if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) != 1)
                            {

                                pay.BackgroundImage = Properties.Resources.nopay;
                                pay.Click += new EventHandler(gathering);
                                pay.Location = new Point(125, 108);


                            }

                            else if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                            {
                                pay.BackgroundImage = Properties.Resources.finish;
                                pay.Size = new Size(66, 15);
                                pay.Location = new Point(115, 108);
                            }




                            if (i >= 3 && i < 6)
                            {
                                remove.BackgroundImage = Properties.Resources.remove2;
                                PanelChild p = new PanelChild();
                                p.Size = new Size(216, 135);
                                p.BackColor = Color.FromArgb(255, 255, 255, 255);
                                p.Location = new Point(1 + j * 216, 136);
                                p.Controls.Add(l1);
                           //    p.Controls.Add(remove);
                                p.Controls.Add(lblTitle);
                                p.Controls.Add(lblTitle3);
    
                                p.location = i;
                                p.Controls.Add(pay);
                                j++;
                                o.orderPanel.Controls.Add(p);
                                p.ID = int.Parse(jarry[i]["ID"].ToString());
                                if (jarry[i]["orderNO"] != null)
                                {
                                    p.orderNO = jarry[i]["orderNO"].ToString();
                                }
                                if (jarry[i]["payType"] != null)
                                {
                                    p.payType = int.Parse(jarry[i]["payType"].ToString());
                                }
                                if (jarry[i]["payNO"] != null)
                                {
                                    p.payNO = jarry[i]["payNO"].ToString();
                                }
                                if (jarry[i]["primeMoney"] != null)
                                {
                                    p.primeMoney = double.Parse(jarry[i]["primeMoney"].ToString());
                                }
                                if (jarry[i]["saleMoney"] != null)
                                {
                                    p.saleMoney = double.Parse(jarry[i]["saleMoney"].ToString());
                                }
                                if (jarry[i]["paidMoney"] != null)
                                {
                                    p.paidMoney = double.Parse(jarry[i]["paidMoney"].ToString());
                                    pay.realityPrice = double.Parse(jarry[i]["paidMoney"].ToString());
                                }
                                if (jarry[i]["vipMoney"] != null)
                                {
                                    p.memberMoney = double.Parse(jarry[i]["vipMoney"].ToString());
                                }
                                if (jarry[i]["serviceStatus"] != null)
                                {
                                    p.serviceStatus = int.Parse(jarry[i]["serviceStatus"].ToString());
                                    if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 1)
                                    {
                                        p.BackgroundImage = Properties.Resources.servicefinsh;

                                        p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                        p.RightToLeft = RightToLeft.Yes;

                                    } else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                    {
                                        p.BackgroundImage = Properties.Resources.orderCancel;
                                        pay.Visible = false;
                                        p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                        p.RightToLeft = RightToLeft.Yes;
                                    }
                                }
                                if (jarry[i]["payStatus"] != null)
                                {
                                    p.payStatus = int.Parse(jarry[i]["payStatus"].ToString());
                                }
                                if (jarry[i]["deskNO"] == null)
                                {
                                    p.ordinal = jarry[i]["ordinal"].ToString();
                                }
                                else
                                {
                                    p.deskName = jarry[i]["deskName"].ToString();
                                    p.deskNO = jarry[i]["deskNO"].ToString();
                                    p.ordinal = jarry[i]["ordinal"].ToString();
                                }

                                p.Click += new EventHandler(showDeatail);
                            }
                            else if (i >= 6)
                            {
                                remove.BackgroundImage = Properties.Resources.remove;
                                PanelChild p = new PanelChild();
                                p.Size = new Size(216, 135);
                                p.BackColor = Color.FromArgb(255, 221, 221, 221);
                                p.Location = new Point(1 + k * 216, 272);
                                p.Controls.Add(l1);
                             //   p.Controls.Add(remove);
                                p.location = i;
                                p.Controls.Add(lblTitle);
                                p.Controls.Add(lblTitle3);
                          
                                p.location = i;
                                p.Controls.Add(pay);
                                k++;
                                o.orderPanel.Controls.Add(p);
                                p.ID = int.Parse(jarry[i]["ID"].ToString());
                                if (jarry[i]["orderNO"] != null)
                                {
                                    p.orderNO = jarry[i]["orderNO"].ToString();
                                }
                                if (jarry[i]["payType"] != null)
                                {
                                    p.payType = int.Parse(jarry[i]["payType"].ToString());
                                }
                                if (jarry[i]["payNO"] != null)
                                {
                                    p.payNO = jarry[i]["payNO"].ToString();
                                }
                                if (jarry[i]["primeMoney"] != null)
                                {
                                    p.primeMoney = double.Parse(jarry[i]["primeMoney"].ToString());

                                }
                                if (jarry[i]["saleMoney"] != null)
                                {
                                    p.saleMoney = double.Parse(jarry[i]["saleMoney"].ToString());
                                }
                                if (jarry[i]["paidMoney"] != null)
                                {
                                    p.paidMoney = double.Parse(jarry[i]["paidMoney"].ToString());
                                    pay.realityPrice = double.Parse(jarry[i]["paidMoney"].ToString());
                                }
                                if (jarry[i]["vipMoney"] != null)
                                {
                                    p.memberMoney = double.Parse(jarry[i]["vipMoney"].ToString());
                                }
                                if (jarry[i]["serviceStatus"] != null)
                                {
                                    p.serviceStatus = int.Parse(jarry[i]["serviceStatus"].ToString());
                                    if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 1)
                                    {
                                        p.BackgroundImage = Properties.Resources.servicefinsh;

                                        p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                        p.RightToLeft = RightToLeft.Yes;
                                    }
                                    else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                    {
                                        p.BackgroundImage = Properties.Resources.orderCancel;

                                        p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
                                        pay.Visible = false;
                                        p.RightToLeft = RightToLeft.Yes;
                                    }
                                }
                                if (jarry[i]["payStatus"] != null)
                                {
                                    p.payStatus = int.Parse(jarry[i]["payStatus"].ToString());
                                }
                                if (jarry[i]["deskNO"] == null)
                                {
                                    p.ordinal = jarry[i]["ordinal"].ToString();
                                }
                                else
                                {
                                    p.deskNO = jarry[i]["deskNO"].ToString();
                                    p.deskName = jarry[i]["deskName"].ToString();
                                    p.ordinal = jarry[i]["ordinal"].ToString();
                                }
                                p.Click += new EventHandler(showDeatail);
                            }
                            else
                            {
                                remove.BackgroundImage = Properties.Resources.remove;
                                PanelChild p = new PanelChild();
                                p.Size = new Size(216, 135);
                                p.BackColor = Color.FromArgb(255, 221, 221, 221);
                                p.Location = new Point(1 + i * 216, 0);
                                p.Controls.Add(l1);
                             //   p.Controls.Add(remove);
                                p.Controls.Add(lblTitle);
                                p.Controls.Add(lblTitle3);
                        

                                p.Controls.Add(pay);
                                o.orderPanel.Controls.Add(p);

                                p.orderNO = jarry[i]["orderNO"].ToString();
                                p.ID = int.Parse(jarry[i]["ID"].ToString());
                                p.createTime = long.Parse(jarry[i]["createTime"].ToString());

                                if (jarry[i]["orderNO"] != null)
                                {
                                    p.orderNO = jarry[i]["orderNO"].ToString();
                                }
                                if (jarry[i]["payType"] != null)
                                {
                                    p.payType = int.Parse(jarry[i]["payType"].ToString());
                                }
                                if (jarry[i]["payNO"] != null)
                                {
                                    p.payNO = jarry[i]["payNO"].ToString();
                                }
                                if (jarry[i]["primeMoney"] != null)
                                {
                                    p.primeMoney = double.Parse(jarry[i]["primeMoney"].ToString());
                                }
                                if (jarry[i]["saleMoney"] != null)
                                {
                                    p.saleMoney = double.Parse(jarry[i]["saleMoney"].ToString());
                                }
                                if (jarry[i]["paidMoney"] != null)
                                {
                                    p.paidMoney = double.Parse(jarry[i]["paidMoney"].ToString());
                                    pay.realityPrice = double.Parse(jarry[i]["paidMoney"].ToString());
                                }
                                if (jarry[i]["vipMoney"] != null)
                                {
                                    p.memberMoney = double.Parse(jarry[i]["vipMoney"].ToString());
                                }
                                if (jarry[i]["serviceStatus"] != null)
                                {
                                    p.serviceStatus = int.Parse(jarry[i]["serviceStatus"].ToString());
                                    if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 1)
                                    {
                                        p.BackgroundImage = Properties.Resources.servicefinsh;

                                        p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                        p.RightToLeft = RightToLeft.Yes;
                                    }
                                    else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                    {
                                        p.BackgroundImage = Properties.Resources.orderCancel;

                                        p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
                                        pay.Visible = false;
                                        p.RightToLeft = RightToLeft.Yes;
                                    }
                                }
                                if (jarry[i]["payStatus"] != null)
                                {
                                    p.payStatus = int.Parse(jarry[i]["payStatus"].ToString());
                                }

                                if (jarry[i]["deskNO"] == null)
                                {
                                    p.ordinal = jarry[i]["ordinal"].ToString();
                                }
                                else
                                {
                                    p.deskNO = jarry[i]["deskNO"].ToString();
                                    p.ordinal = jarry[i]["ordinal"].ToString();
                                    p.deskName = jarry[i]["deskName"].ToString();
                                }


                                p.Click += new EventHandler(showDeatail);
                            }
                        }
                    }
                    else
                    {
                        MessageBox.Show("暂无订单");
                    }
                }
                else
                {
                    MessageBox.Show(jo["message"].ToString());

                }

            }

        }

        private void button4_Click(object sender, EventArgs e)
        {
            if (this.button4.Text == "开启")
            {
                this.button4.Text = "关闭";
                this.button4.BackgroundImage = Properties.Resources.close;
              //   deviceState = false;
            }
            else if (this.button4.Text == "关闭")
            {
                this.button4.Text = "开启";

                this.button4.BackgroundImage = Properties.Resources.open;
              //  deviceState = true;
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.WindowState = FormWindowState.Minimized;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (MessageBox.Show("您确定要退出骏骐点餐系统吗？", "确认退出", MessageBoxButtons.OKCancel, MessageBoxIcon.Question, MessageBoxDefaultButton.Button2) == DialogResult.OK)
            {
                isClose(0);
                this.Close();
                this.Dispose();
                Application.Exit();
            }
        }

        private void payorder_Click(object sender, EventArgs e)
        {
            try
            {
                orderNo = null;
                p.se = 0;
                this.payorder.BackgroundImage = Properties.Resources.pay;
                this.order.BackgroundImage = Properties.Resources.orderleave;
                this.desk.BackgroundImage = Properties.Resources.deskleave;
                this.panelAll.Controls.Clear();

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 81 }, { "token", Login.userInfo.token } };
                JObject json3 = new JObject { { "todaySum", 1 }, { "totalCount", 1 } };

                json2.Add("data", json3.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                Console.WriteLine("json2.ToString():" + json2.ToString());
                getPays(result);
                p.pageIndex.Text = "1";
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        public void getPays(string result)
        {

            if (result != string.Empty)
            {
                Console.WriteLine("pay" + result);
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                if (jo["errorCode"].ToString().Equals("0"))
                {

                    List<PaySimple> pays = new List<PaySimple>();
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                    if (int.Parse(jsonObect["count"].ToString()) > 0)
                    {
                        p.count.Text = jsonObect["totalCount"].ToString();
                        int page = int.Parse(jsonObect["totalCount"].ToString()) / 20;
                        double y = double.Parse(jsonObect["totalCount"].ToString()) % 20;
                        if (y > 0)
                        {
                            page += 1;
                        }
                        p.pageIndex.Text = "1";
                        p.page.Text = page.ToString();
                        p.label7.Text = "￥" + jsonObect["totalSum"].ToString();
                        p.label2.Text = "￥" + jsonObect["todaySum"].ToString();
                        JArray payArray = JArray.Parse(jsonObect["items"].ToString());
                        for (int i = 0; i < payArray.Count; i++)
                        {



                            PaySimple paySimple = new PaySimple();
                            paySimple.ID = int.Parse(payArray[i]["ID"].ToString());
                            paySimple.payNO = payArray[i]["payNO"].ToString();

                            if (int.Parse(payArray[i]["payType"].ToString()) == 1)
                            {
                                paySimple.payType = "微信";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 2)
                            {
                                paySimple.payType = "支付宝";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 3)
                            {
                                paySimple.payType = "现金";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 4)
                            {
                                paySimple.payType = "刷卡";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 5)
                            {
                                paySimple.payType = "其他";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 10)
                            {
                                paySimple.payType = "会员卡";
                            }
                            else
                            {
                                paySimple.payType = "其他";
                            }

                            paySimple.orderNO = payArray[i]["orderNO"].ToString();
                            if (payArray[i]["sum"]!=null)
                            {
                                paySimple.sum = double.Parse(payArray[i]["sum"].ToString());

                            }
                         

                            if (int.Parse(payArray[i]["state"].ToString()) == 0)
                            {
                                paySimple.state = "支付失败";
                            }
                            else
                            if (int.Parse(payArray[i]["state"].ToString()) == 1)
                            {
                                paySimple.state = "已支付";
                            }
                            else
                            if (int.Parse(payArray[i]["state"].ToString()) == 2)
                            {
                                paySimple.state = "已退款";
                            }
                            if (payArray[i]["nickName"] != null)
                            {
                                paySimple.nickName = payArray[i]["nickName"].ToString();
                            }

                            System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                            DateTime dt = startTime.AddMilliseconds(long.Parse(payArray[i]["createTime"].ToString()));
                            paySimple.createTime = dt.ToString("yyyy/MM/dd HH:mm:ss");

                            if (payArray[i]["paySubType"] != null)
                            {
                                if (int.Parse(payArray[i]["paySubType"].ToString()) == 1)
                                {
                                    paySimple.paySubType = "付款码";
                                }
                                else
                                 if (int.Parse(payArray[i]["paySubType"].ToString()) == 2)
                                {
                                    paySimple.paySubType = "收款码";
                                }
                                else
                                 if (int.Parse(payArray[i]["paySubType"].ToString()) == 2)
                                {
                                    paySimple.paySubType = "支付码";
                                }
                            }


                            pays.Add(paySimple);
                        }
                        p.dataGridView1.DataSource = pays;

                        p.Show();

                        this.panelAll.Controls.Add(p);
                    }
                    else
                    {
                        MessageBox.Show("暂无数据");
                    }

                }
            }
        }
        public static void getSearchPays(string result)
        {

            if (result != string.Empty)
            {
                Console.WriteLine("pay" + result);
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                if (jo["errorCode"].ToString().Equals("0"))
                {

                    List<PaySimple> pays = new List<PaySimple>();
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                    if (int.Parse(jsonObect["count"].ToString()) > 0)
                    {
                        p.count.Text = jsonObect["totalCount"].ToString();
                        int page = int.Parse(jsonObect["totalCount"].ToString()) / 20;
                        double y = double.Parse(jsonObect["totalCount"].ToString()) % 20;
                        if (y > 0)
                        {
                            page += 1;
                        }
                        
                        p.page.Text = page.ToString();
                        p.label7.Text = jsonObect["totalSum"].ToString();
                        p.label2.Text = jsonObect["todaySum"].ToString();
                        JArray payArray = JArray.Parse(jsonObect["items"].ToString());
                        for (int i = 0; i < payArray.Count; i++)
                        {

                            PaySimple paySimple = new PaySimple();
                            paySimple.ID = int.Parse(payArray[i]["ID"].ToString());
                            paySimple.payNO = payArray[i]["payNO"].ToString();

                            if (int.Parse(payArray[i]["payType"].ToString()) == 1)
                            {
                                paySimple.payType = "微信";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 2)
                            {
                                paySimple.payType = "支付宝";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 3)
                            {
                                paySimple.payType = "现金";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 4)
                            {
                                paySimple.payType = "刷卡";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 5)
                            {
                                paySimple.payType = "其他";
                            }
                            else
                            if (int.Parse(payArray[i]["payType"].ToString()) == 10)
                            {
                                paySimple.payType = "会员卡";
                            }

                            paySimple.orderNO = payArray[i]["orderNO"].ToString();
                            if (payArray[i]["sum"] != null)
                            {
                                paySimple.sum = double.Parse(payArray[i]["sum"].ToString());
                            }


                            if (int.Parse(payArray[i]["state"].ToString()) == 0)
                            {
                                paySimple.state = "支付失败";
                            }
                            else
                            if (int.Parse(payArray[i]["state"].ToString()) == 1)
                            {
                                paySimple.state = "已支付";
                            }
                            else
                            if (int.Parse(payArray[i]["state"].ToString()) == 2)
                            {
                                paySimple.state = "已退款";
                            }
                            if (payArray[i]["nickName"] != null)
                            {
                                paySimple.nickName = payArray[i]["nickName"].ToString();
                            }
                            System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                            DateTime dt = startTime.AddMilliseconds(long.Parse(payArray[i]["createTime"].ToString()));
                            paySimple.createTime = dt.ToString("yyyy/MM/dd HH:mm:ss");
                            if (payArray[i]["paySubType"] != null)
                            {
                                if (int.Parse(payArray[i]["paySubType"].ToString()) == 1)
                                {
                                    paySimple.paySubType = "付款码";
                                }
                                else
                              if (int.Parse(payArray[i]["paySubType"].ToString()) == 2)
                                {
                                    paySimple.paySubType = "收款码";
                                }
                                else
                              if (int.Parse(payArray[i]["paySubType"].ToString()) == 2)
                                {
                                    paySimple.paySubType = "支付码";
                                }
                            }


                            pays.Add(paySimple);
                        }
                        p.dataGridView1.DataSource = pays;

                    }
                    else
                    {
                        MessageBox.Show("暂无数据");
                    }

                }
            }
        }
        public static void gathering(object sender, EventArgs e)
        {
            try {
                ButtonChild button = (ButtonChild)sender;
                TestForm testForm = new TestForm(button.orderNo, button.handlePrice, button.reducedPrice, button.realityPrice,false);

                testForm.ShowDialog();
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        public static void payShow(object sender, EventArgs e)
        {
            try
            {
                ButtonChild button = (ButtonChild)sender;
                TestForm testForm = new TestForm(button.orderNo, button.handlePrice, button.reducedPrice, button.realityPrice, true);

                testForm.ShowDialog();
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
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

        //显示订单详情
        public static void showDeatail(object sender, EventArgs e)
        {

           // try
          //  {
                if (panelArray[0] != null)
                {
                    if (panelArray[0].location >= 4 && panelArray[0].location < 8)
                    {
                        panelArray[0].BackColor = Color.FromArgb(255, 255, 255, 255);
                    }
                    else
                    {
                        panelArray[0].BackColor = Color.FromArgb(255, 221, 221, 221);
                    }

                }

                PanelChild p = (PanelChild)sender;
                panelArray[0] = p;
                payNo = p.payNO;
                orderId = p.ID;
                orderNo = p.orderNO;
                payPrice = p.paidMoney;
                sellState = p.payStatus;
                p.BackColor = Color.FromArgb(40, 189, 189, 189);

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 86 }, { "muuid", Login.userInfo.muuid } };
                JObject json3 = new JObject { { "orderNO", p.orderNO } };

                json2.Add("data", json3.ToString());

                string result = string.Empty;
                result = HttpPostData(testurl.ToString(), json2.ToString());
                if (result != string.Empty)
                {
                    Console.WriteLine("dedatil" + result);
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                        int top = 2;
                        int left = 10;
                        Label l1 = new Label();
                        Label l2 = new Label();
                        Label l3 = new Label();
                        Label l4 = new Label();
                        Label l5 = new Label();
                        Label l6 = new Label();
                        Label l7 = new Label();
                        Label l9 = new Label();
                        Label l10 = new Label();
                        Label l11 = new Label();

                        ButtonChild print = new ButtonChild();
                        print.FlatStyle = FlatStyle.Flat;
                        print.FlatAppearance.BorderSize = 0;
                        print.Size = new Size(58, 26);

                        print.BackgroundImage = Properties.Resources.print;
                        print.Click += new EventHandler(printOrderDetail);
                        print.orderNo = p.orderNO;

                        ButtonChild printAgin = new ButtonChild();
                        printAgin.FlatStyle = FlatStyle.Flat;
                        printAgin.FlatAppearance.BorderSize = 0;
                        printAgin.Size = new Size(58, 26);

                        printAgin.BackgroundImage = Properties.Resources.printAgin;
                        printAgin.Click += new EventHandler(printAginOrderDetail);
                        printAgin.orderNo = p.orderNO;

                        l5.Font = new Font("微软雅黑", 10);
                        if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 0)
                        {
                            l5.Name = "未支付";
                        }
                        else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 2)
                        {
                            l5.Name = "记账";

                        }

                        else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 5)
                        {
                            l5.Name = "支付失败";

                        }
                        else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 6)
                        {
                            l5.Name = "支付中";

                        }
                        else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 7)
                        {
                            l5.Name = "退款中";

                        }
                        else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 8)
                        {
                            l5.Name = "退款失败";

                        }
                        else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 9)
                        {
                            l5.Name = "已退款";

                        }
                        else
                        {
                            if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 1)
                            {
                                l5.Name = "微信支付";
                            }
                            else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 2)
                            {
                                l5.Name = "支付宝支付";
                            }
                            else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 3)
                            {
                                l5.Name = "现金支付";
                            }
                            else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 4)
                            {
                                l5.Name = "刷卡支付";
                            }
                            else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 5)
                            {
                                l5.Name = "其他支付";
                            }
                            else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 10)
                            {
                                l5.Name = "会员卡支付";
                            }
                        }
                        l5.Text = l5.Name;
                        l5.ForeColor = Color.Gray;
                        l5.Location = new Point(199, 38);

                        l9.Size = new Size(250, 20);
                        if (jsonObect["primeMoney"]!=null)
                        {
                            l9.Name = "应收金额：￥" + jsonObect["primeMoney"].ToString();
                        }
                       
                        l9.Text = l9.Name;
                        l9.Left = left;
                        l9.Font = new Font("微软雅黑", 10);
                        l9.ForeColor = Color.Gray;

                        if (jsonObect["vipMoney"] != null)
                        {
                            l10.Name = "会员价：￥" + jsonObect["vipMoney"].ToString();
                        }
                        l10.Size = new Size(250, 20);
                       
                        l10.Text = l10.Name;
                        l10.Left = left;
                        l10.Font = new Font("微软雅黑", 10);
                        l10.ForeColor = Color.Gray;


                        l7.Name = "订单详情";
                        l7.Text = l7.Name;
                        l7.Left = left;
                        l7.Size = new Size(100, 20);
                        l7.Top = top + l1.Height;
                        l7.Font = new Font("微软雅黑", 12);
                        l7.ForeColor = Color.Gray;


                        l11.Size = new Size(250, 20);
                        l11.Name = "实收金额：￥" + jsonObect["paidMoney"].ToString();
                        l11.Text = l11.Name;
                        l11.Left = left;
                        l11.Font = new Font("微软雅黑", 10);
                        l11.ForeColor = Color.Gray;

                        Label cpName = new Label();
                        // cpName.Size = new Size(20, 20);
                        cpName.Name = "名称";
                        cpName.Text = cpName.Name;
                        cpName.Left = left;
                        cpName.Font = new Font("微软雅黑", 10);
                        cpName.ForeColor = Color.Gray;

                        Label price = new Label();
                        //  price.Size = new Size(20, 20);
                        price.Name = "价格/会员价格";
                        price.Text = price.Name;
                        price.Left = left;
                        price.Font = new Font("微软雅黑", 10);
                        price.ForeColor = Color.Gray;

                        Label count = new Label();
                        // count.Size = new Size(20, 20);
                        count.Name = "数量";
                        count.Text = count.Name;
                        count.Left = left;
                        count.Font = new Font("微软雅黑", 10);
                        count.ForeColor = Color.Gray;

                        o.detailPanel.Controls.Clear();



                        if (p.deskNO != null || jsonObect["deskName"] != null)
                        {
                            l1.Name = jsonObect["deskName"].ToString();
                            l1.Text = l1.Name;
                            l1.Left = left;
                            l1.Size = new Size(170, 50);
                            l1.Top = top + l1.Height;
                            l1.Font = new Font("微软雅黑", 20);
                            l1.ForeColor = Color.Black;
                            l1.Location = new Point(16, 17);

                            l4.Name = jsonObect["ordinal"].ToString();
                            l4.Text = l4.Name;
                            l4.Font = new Font("微软雅黑", 10);
                            l4.ForeColor = Color.Gray;
                            l4.Location = new Point(199, 13);


                        }
                        else
                        {

                            l1.Name = jsonObect["ordinal"].ToString();
                            l1.Text = l1.Name;
                            l1.Left = left;
                            l1.Size = new Size(170, 50);
                            l1.Top = top + l1.Height;
                            l1.Font = new Font("微软雅黑", 20);
                            l1.ForeColor = Color.Black;
                            l1.Location = new Point(16, 17);
                        }


                        if (int.Parse(jsonObect["payStatus"].ToString())!= 0)
                        {
                            l3.Size = new Size(250, 20);
                            l3.Name = "支付金额：￥" + jsonObect["paidMoney"].ToString();
                            l3.Text = l3.Name;
                            l3.Left = left;
                            l3.Font = new Font("微软雅黑", 10);
                            l3.ForeColor = Color.Gray;
                            l3.Location = new Point(16, 85);

                            if (jsonObect["payTime"] != null)
                            {
                                System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区             
                                DateTime dt = startTime.AddMilliseconds(long.Parse(jsonObect["payTime"].ToString()));
                                l2.Name = "支付时间：" + dt.ToString("yyyy/MM/dd HH:mm:ss");

                            }

                            l2.Size = new Size(250, 20);

                            l2.Text = l2.Name;
                            l2.Left = left;
                            l2.Font = new Font("微软雅黑", 10);
                            l2.ForeColor = Color.Gray;
                            l2.Location = new Point(16, 65);


                            l6.Size = new Size(250, 20);
                            l6.AutoSize = true;
                            l6.Name = "订单编号：" + p.orderNO;
                            //l6.Name = "订单编号：" + "43d1533966464160d";
                            l6.Text = l6.Name;
                            l6.Left = left;
                            l6.Font = new Font("微软雅黑", 10);
                            l6.ForeColor = Color.Gray;
                            l6.Location = new Point(16, 105);
                            JArray attachedArray = new JArray();
                            if (jsonObect["attached"] != null)
                            {
                                attachedArray = JArray.Parse(jsonObect["attached"].ToString());
                            }
                            if (attachedArray.Count > 0)
                            {
                                for (int i = 0; i < attachedArray.Count; i++)
                                {
                                    Label cp = new Label();
                                    cp.AutoSize = true;
                                    cp.Size = new Size(125, 20);
                                    cp.Name = attachedArray[i]["name"].ToString();


                                    cp.Left = left;
                                    if (attachedArray[i]["delFlag"] != null && attachedArray[i]["delFlag"].ToString().Equals("-1"))
                                    {
                                        cp.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                                    }
                                    else
                                    {
                                        cp.Font = new Font("微软雅黑", 10);
                                    }
                                    cp.ForeColor = Color.Gray;
                                    cp.Location = new Point(16, 235 + i * 20);

                                    if (attachedArray[i]["attrs"] != null)
                                    {
                                        cp.Name += "(";
                                        JArray attrsArray = JArray.Parse(attachedArray[i]["attrs"].ToString());
                                        for (int j = 0; j < attrsArray.Count; j++)
                                        {

                                            if (j == attrsArray.Count - 1)
                                            {
                                                cp.Name += attrsArray[j]["text"].ToString();
                                            }
                                            else
                                            {
                                                cp.Name += attrsArray[j]["text"].ToString() + ",";
                                            }
                                        }
                                        cp.Name += ")";
                                    }
                                    cp.Text = cp.Name;

                                    Label cpPrice = new Label();
                                    // count.Size = new Size(20, 20);

                                    if (attachedArray[i]["vipPrice"] == null)
                                    {
                                        cpPrice.Name = "￥" + attachedArray[i]["price"].ToString();
                                    }
                                    else
                                    {
                                        cpPrice.Name = "￥" + attachedArray[i]["price"].ToString() + "/￥" + attachedArray[i]["vipPrice"].ToString();
                                    }
                                    cpPrice.Text = cpPrice.Name;
                                    cpPrice.Left = left;
                                    cpPrice.Font = new Font("微软雅黑", 10);
                                    cpPrice.ForeColor = Color.Gray;
                                    cpPrice.Location = new Point(151, 235 + i * 20);

                                    Label cpCount = new Label();
                                    // count.Size = new Size(20, 20);
                                    cpCount.Name = attachedArray[i]["count"].ToString();
                                    cpCount.Text = cpCount.Name;
                                    cpCount.Left = left;
                                    cpCount.Font = new Font("微软雅黑", 10);
                                    cpCount.ForeColor = Color.Gray;
                                    cpCount.Location = new Point(250, 235 + i * 20);
                                    o.detailPanel.Controls.Add(cp);
                                    o.detailPanel.Controls.Add(cpPrice);
                                    o.detailPanel.Controls.Add(cpCount);
                                }
                            }


                            l9.Location = new Point(16, 125);
                            l10.Location = new Point(16, 145);
                            l7.Location = new Point(16, 195);
                            print.Location = new Point(227, 185);
                            printAgin.Location = new Point(160, 185);
                            l11.Location = new Point(16, 165);
                            cpName.Location = new Point(16, 215);
                            price.Location = new Point(151, 215);
                            count.Location = new Point(250, 215);

                            o.detailPanel.Controls.Add(l2);

                            o.detailPanel.Controls.Add(l3);
                            o.detailPanel.Controls.Add(cpName);
                            o.detailPanel.Controls.Add(price);
                            o.detailPanel.Controls.Add(count);
                        }
                        else
                        {
                            l3.Size = new Size(250, 20);
                            l3.Name = "订单编号：" + p.orderNO;
                            l3.Text = l3.Name;
                            l3.Left = left;
                            l3.Font = new Font("微软雅黑", 10);
                            l3.ForeColor = Color.Gray;
                            l3.Location = new Point(16, 65);


                            l9.Location = new Point(16, 85);
                            l10.Location = new Point(16, 105);
                            l11.Location = new Point(16, 125);
                            l7.Location = new Point(16, 155);
                            print.Location = new Point(227, 145);
                            printAgin.Location = new Point(160, 145);
                            cpName.Location = new Point(16, 175);
                            price.Location = new Point(151, 175);
                            count.Location = new Point(250, 175);
                            JArray attachedArray = new JArray();
                            if (jsonObect["attached"] != null)
                            {
                                attachedArray = JArray.Parse(jsonObect["attached"].ToString());
                            }

                            if (attachedArray.Count > 0)
                            {
                                for (int i = 0; i < attachedArray.Count; i++)
                                {
                                    Label cp = new Label();
                                    cp.AutoSize = true;
                                    cp.Size = new Size(125, 20);
                                    cp.Name = attachedArray[i]["name"].ToString();

                                    cp.Left = left;

                                    if (attachedArray[i]["delFlag"] != null && attachedArray[i]["delFlag"].ToString().Equals("-1"))
                                    {
                                        cp.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                                    } else
                                    {
                                        cp.Font = new Font("微软雅黑", 10);
                                    }
                                    cp.ForeColor = Color.Gray;
                                    cp.Location = new Point(16, 195 + i * 20);

                                    if (attachedArray[i]["attrs"] != null)
                                    {
                                        cp.Name += "(";
                                        JArray attrsArray = JArray.Parse(attachedArray[i]["attrs"].ToString());
                                        for (int j = 0; j < attrsArray.Count; j++)
                                        {

                                            if (j == attrsArray.Count - 1)
                                            {
                                                cp.Name += attrsArray[j]["text"].ToString();
                                            }
                                            else
                                            {
                                                cp.Name += attrsArray[j]["text"].ToString() + ",";
                                            }
                                        }
                                        cp.Name += ")";
                                    }
                                    cp.Text = cp.Name;
                                    Label cpPrice = new Label();
                                    // count.Size = new Size(20, 20);

                                    if (attachedArray[i]["vipPrice"] == null)
                                    {
                                        cpPrice.Name = "￥" + attachedArray[i]["price"].ToString();
                                    }
                                    else
                                    {
                                        cpPrice.Name = "￥" + attachedArray[i]["price"].ToString() + "/￥" + attachedArray[i]["vipPrice"].ToString();
                                    }
                                    cpPrice.Text = cpPrice.Name;
                                    cpPrice.Left = left;
                                    cpPrice.Font = new Font("微软雅黑", 10);
                                    cpPrice.ForeColor = Color.Gray;
                                    cpPrice.Location = new Point(151, 195 + i * 20);

                                    Label cpCount = new Label();
                                    // count.Size = new Size(20, 20);
                                    cpCount.Name = attachedArray[i]["count"].ToString();
                                    cpCount.Text = cpCount.Name;
                                    cpCount.Left = left;
                                    cpCount.Font = new Font("微软雅黑", 10);
                                    cpCount.ForeColor = Color.Gray;
                                    cpCount.Location = new Point(250, 195 + i * 20);
                                    o.detailPanel.Controls.Add(cp);
                                    o.detailPanel.Controls.Add(cpPrice);
                                    o.detailPanel.Controls.Add(cpCount);
                                }
                            }



                            o.detailPanel.Controls.Add(l2);
                            o.detailPanel.Controls.Add(l3);
                            o.detailPanel.Controls.Add(cpName);
                            o.detailPanel.Controls.Add(price);
                            o.detailPanel.Controls.Add(count);

                        }

                        o.detailPanel.Controls.Add(l1);
                        o.detailPanel.Controls.Add(l3);
                        o.detailPanel.Controls.Add(l4);
                        o.detailPanel.Controls.Add(l5);
                        o.detailPanel.Controls.Add(l6);
                        o.detailPanel.Controls.Add(l7);
                        o.detailPanel.Controls.Add(l9);
                        o.detailPanel.Controls.Add(l10);
                        o.detailPanel.Controls.Add(print);
                        o.detailPanel.Controls.Add(printAgin);
                        o.detailPanel.Controls.Add(l11);

                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                    }

                }

           // }
          //  catch (Exception q) { MessageBox.Show(q.Message); }

        }
        //预约详情
        public static void showReserveDeatail(object sender, EventArgs e)
        {

             try
              {
            if (panelArray[0] != null)
            {
                if (panelArray[0].location >= 4 && panelArray[0].location < 8)
                {
                    panelArray[0].BackColor = Color.FromArgb(255, 255, 255, 255);
                }
                else
                {
                    panelArray[0].BackColor = Color.FromArgb(255, 221, 221, 221);
                }

            }

            PanelChild p = (PanelChild)sender;
            panelArray[0] = p;
            payNo = p.payNO;
            orderId = p.ID;
            orderNo = p.orderNO;
            payPrice = p.paidMoney;
            sellState = p.payStatus;
            p.BackColor = Color.FromArgb(40, 189, 189, 189);

            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 86 }, { "muuid", Login.userInfo.muuid } };
            JObject json3 = new JObject { { "orderNO", p.orderNO } };

            json2.Add("data", json3.ToString());

            string result = string.Empty;
            result = HttpPostData(testurl.ToString(), json2.ToString());
            if (result != string.Empty)
            {
                Console.WriteLine("reservededatil" + result);
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                if (jo["errorCode"].ToString().Equals("0"))
                {
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                    int top = 2;
                    int left = 10;
                    Label l1 = new Label();
                    Label l2 = new Label();
                    Label l3 = new Label();
                    Label l4 = new Label();
                    Label l5 = new Label();
                    Label l6 = new Label();
                    Label l7 = new Label();
                    Label l9 = new Label();
                    Label l11 = new Label();                  

                    l5.Font = new Font("微软雅黑", 10);
                    if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 0)
                    {
                        l5.Name = "未支付";
                    }
                    else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 2)
                    {
                        l5.Name = "记账";

                    }

                    else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 5)
                    {
                        l5.Name = "支付失败";

                    }
                    else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 6)
                    {
                        l5.Name = "支付中";

                    }
                    else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 7)
                    {
                        l5.Name = "退款中";

                    }
                    else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 8)
                    {
                        l5.Name = "退款失败";

                    }
                    else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 9)
                    {
                        l5.Name = "已退款";

                    }
                    else
                    {
                        if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 1)
                        {
                            l5.Name = "微信支付";
                        }
                        else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 2)
                        {
                            l5.Name = "支付宝支付";
                        }
                        else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 3)
                        {
                            l5.Name = "现金支付";
                        }
                        else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 4)
                        {
                            l5.Name = "刷卡支付";
                        }
                        else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 5)
                        {
                            l5.Name = "其他支付";
                        }
                        else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 10)
                        {
                            l5.Name = "会员卡支付";
                        }
                    }
                    l5.Text = l5.Name;
                    l5.ForeColor = Color.Gray;
                    l5.Location = new Point(199, 38);

                    l9.Size = new Size(250, 20);
                    l9.Name = "应收定金金额：￥" + jsonObect["primeMoney"].ToString();
                    l9.Text = l9.Name;
                    l9.Left = left;
                    l9.Font = new Font("微软雅黑", 10);
                    l9.ForeColor = Color.Gray;


                    l7.Name = "预约详情";
                    l7.Text = l7.Name;
                    l7.Left = left;
                    l7.Size = new Size(100, 20);
                    l7.Top = top + l1.Height;
                    l7.Font = new Font("微软雅黑", 12);
                    l7.ForeColor = Color.Gray;


                    l11.Size = new Size(250, 20);
                    l11.Name = "实收金额：￥" + jsonObect["paidMoney"].ToString();
                    l11.Text = l11.Name;
                    l11.Left = left;
                    l11.Font = new Font("微软雅黑", 10);
                    l11.ForeColor = Color.Gray;

                    Label cpName = new Label();
                    // cpName.Size = new Size(20, 20);
                    cpName.Name = "名称";
                    cpName.Text = cpName.Name;
                    cpName.Left = left;
                    cpName.Font = new Font("微软雅黑", 10);
                    cpName.ForeColor = Color.Gray;

                    Label price = new Label();
                    //  price.Size = new Size(20, 20);
                    price.Name = "价格/会员价格";
                    price.Text = price.Name;
                    price.Left = left;
                    price.Font = new Font("微软雅黑", 10);
                    price.ForeColor = Color.Gray;

                    Label count = new Label();
                    // count.Size = new Size(20, 20);
                    count.Name = "数量";
                    count.Text = count.Name;
                    count.Left = left;
                    count.Font = new Font("微软雅黑", 10);
                    count.ForeColor = Color.Gray;

                    o.detailPanel.Controls.Clear();
                   
                    l1.Name = jsonObect["ordinal"].ToString();
                    l1.Text = l1.Name;
                    l1.Left = left;
                    l1.Size = new Size(170, 50);
                    l1.Top = top + l1.Height;
                    l1.Font = new Font("微软雅黑", 20);
                    l1.ForeColor = Color.Black;
                    l1.Location = new Point(16, 17);
                    

                    if (int.Parse(jsonObect["payStatus"].ToString()) != 0)
                    {
                        l3.Size = new Size(250, 20);
                        l3.Name = "支付金额：￥" + jsonObect["paidMoney"].ToString();
                        l3.Text = l3.Name;
                        l3.Left = left;
                        l3.Font = new Font("微软雅黑", 10);
                        l3.ForeColor = Color.Gray;
                        l3.Location = new Point(16, 85);

                        if (jsonObect["payTime"] != null)
                        {
                            System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区             
                            DateTime dt = startTime.AddMilliseconds(long.Parse(jsonObect["payTime"].ToString()));
                            l2.Name = "支付时间：" + dt.ToString("yyyy/MM/dd HH:mm:ss");

                        }

                        l2.Size = new Size(250, 20);

                        l2.Text = l2.Name;
                        l2.Left = left;
                        l2.Font = new Font("微软雅黑", 10);
                        l2.ForeColor = Color.Gray;
                        l2.Location = new Point(16, 65);


                        l6.Size = new Size(250, 20);
                        l6.Name = "订单编号：" + p.orderNO;
                        l6.Text = l6.Name;
                        l6.Left = left;
                        l6.Font = new Font("微软雅黑", 10);
                        l6.ForeColor = Color.Gray;
                        l6.Location = new Point(16, 105);
                        JArray attachedArray = new JArray();
                        if (jsonObect["attached"] != null)
                        {
                            attachedArray = JArray.Parse(jsonObect["attached"].ToString());
                        }
                        if (attachedArray.Count > 0)
                        {
                            for (int i = 0; i < attachedArray.Count; i++)
                            {
                                Label cp = new Label();
                                cp.AutoSize = true;
                                cp.Size = new Size(125, 20);
                                cp.Name = attachedArray[i]["name"].ToString();


                                cp.Left = left;
                                if (attachedArray[i]["delFlag"] != null && attachedArray[i]["delFlag"].ToString().Equals("-1"))
                                {
                                    cp.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                                }
                                else
                                {
                                    cp.Font = new Font("微软雅黑", 10);
                                }
                                cp.ForeColor = Color.Gray;
                                cp.Location = new Point(16, 215 + i * 20);

                                if (attachedArray[i]["attrs"] != null)
                                {
                                    cp.Name += "(";
                                    JArray attrsArray = JArray.Parse(attachedArray[i]["attrs"].ToString());
                                    for (int j = 0; j < attrsArray.Count; j++)
                                    {

                                        if (j == attrsArray.Count - 1)
                                        {
                                            cp.Name += attrsArray[j]["text"].ToString();
                                        }
                                        else
                                        {
                                            cp.Name += attrsArray[j]["text"].ToString() + ",";
                                        }
                                    }
                                    cp.Name += ")";
                                }
                                cp.Text = cp.Name;

                                Label cpPrice = new Label();
                                // count.Size = new Size(20, 20);

                                if (attachedArray[i]["vipPrice"] == null)
                                {
                                    cpPrice.Name = "￥" + attachedArray[i]["price"].ToString();
                                }
                                else
                                {
                                    cpPrice.Name = "￥" + attachedArray[i]["price"].ToString() + "/￥" + attachedArray[i]["vipPrice"].ToString();
                                }
                                cpPrice.Text = cpPrice.Name;
                                cpPrice.Left = left;
                                cpPrice.Font = new Font("微软雅黑", 10);
                                cpPrice.ForeColor = Color.Gray;
                                cpPrice.Location = new Point(151, 215 + i * 20);

                                Label cpCount = new Label();
                                // count.Size = new Size(20, 20);
                                cpCount.Name = attachedArray[i]["count"].ToString();
                                cpCount.Text = cpCount.Name;
                                cpCount.Left = left;
                                cpCount.Font = new Font("微软雅黑", 10);
                                cpCount.ForeColor = Color.Gray;
                                cpCount.Location = new Point(250, 215 + i * 20);
                                o.detailPanel.Controls.Add(cp);
                                o.detailPanel.Controls.Add(cpPrice);
                                o.detailPanel.Controls.Add(cpCount);
                            }
                        }


                        l9.Location = new Point(16, 125);
                        l7.Location = new Point(16, 175);
                        l11.Location = new Point(16, 145);
                        cpName.Location = new Point(16, 195);
                        price.Location = new Point(151, 195);
                        count.Location = new Point(250, 195);

                        o.detailPanel.Controls.Add(l2);
                        o.detailPanel.Controls.Add(l3);
                        o.detailPanel.Controls.Add(cpName);
                        o.detailPanel.Controls.Add(price);
                        o.detailPanel.Controls.Add(count);
                    }
                  

                    o.detailPanel.Controls.Add(l1);
                    o.detailPanel.Controls.Add(l3);
                    o.detailPanel.Controls.Add(l4);
                    o.detailPanel.Controls.Add(l5);
                    o.detailPanel.Controls.Add(l6);
                    o.detailPanel.Controls.Add(l7);
                    o.detailPanel.Controls.Add(l9);
                    o.detailPanel.Controls.Add(l11);

                }
                else
                {
                    MessageBox.Show(jo["message"].ToString());
                }

            }

             }
              catch (Exception q) { MessageBox.Show(q.Message); }

        }
        private void button2_MouseHover(object sender, EventArgs e)
        {
            this.button2.BackgroundImage = Properties.Resources.exit_hover;
        }

        private void button2_MouseLeave(object sender, EventArgs e)
        {
            this.button2.BackgroundImage = Properties.Resources.exit;
        }



        private void button3_MouseLeave(object sender, EventArgs e)
        {
            this.button3.BackgroundImage = Properties.Resources.min;
        }

        private void button3_MouseHover(object sender, EventArgs e)
        {
            this.button3.BackgroundImage = Properties.Resources.min_hover;
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
          
        }
        public static string HttpPostData(string url, string param)
        {
            var result = string.Empty;
            try
            {

                //注意提交的编码 这边是需要改变的 这边默认的是Default：系统当前编码
                byte[] postData = Encoding.UTF8.GetBytes(param);

                // 设置提交的相关参数 
                HttpWebRequest request = WebRequest.Create(url) as HttpWebRequest;
                Encoding myEncoding = Encoding.UTF8;
                request.Method = "POST";
                request.KeepAlive = false;
                request.AllowAutoRedirect = true;
                request.ContentType = "application/x-www-form-urlencoded";
                request.UserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR  3.0.04506.648; .NET CLR 3.5.21022; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";
                request.ContentLength = postData.Length;

                // 提交请求数据 
                System.IO.Stream outputStream = request.GetRequestStream();
                outputStream.Write(postData, 0, postData.Length);
                outputStream.Close();

                HttpWebResponse response;
                Stream responseStream;
                StreamReader reader;
                string srcString;
                response = request.GetResponse() as HttpWebResponse;
                responseStream = response.GetResponseStream();
                reader = new System.IO.StreamReader(responseStream, Encoding.GetEncoding("UTF-8"));
                srcString = reader.ReadToEnd();
                result = srcString;   //返回值赋值
                reader.Close();
            }
            catch (Exception e)
            {
                MessageBox.Show("服务器异常" + e.Message);
            }
            return result;
        }
        public static void getOrders(string param)
        {
            string result = string.Empty;
            result = HttpPostData(testurl.ToString(), param);
            if (result != string.Empty)
            {
                Console.WriteLine(result);
                if (result != null)
                {
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                    if (jo["errorCode"].ToString().Equals("0"))
                    {

                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                        if (int.Parse(jsonObect["count"].ToString()) > 0)
                        {

                            o.count.Text = jsonObect["totalCount"].ToString();
                         
                            int page = int.Parse(jsonObect["totalCount"].ToString()) / 9;
                            double y = double.Parse(jsonObect["totalCount"].ToString()) % 9;
                            if (y > 0)
                            {
                                page += 1;
                            }

                            o.page.Text = page.ToString();
                            JArray jarry = JArray.Parse(jsonObect["items"].ToString());

                            int top = 2;
                            int left = 6;
                            int j = 0;
                            int k = 0;
                            o.orderPanel.Controls.Clear();
                            for (int i = 0; i < jarry.Count; i++)
                            {
                                Label l1 = new Label();
                                l1.BackColor = Color.Transparent;
                                if (jarry[i]["deskName"] != null)
                                {
                                    l1.Name = "桌台：" + jarry[i]["deskName"].ToString();
                                    l1.AutoSize = true;
                                    l1.Text = l1.Name;
                                    l1.Left = left;
                                    l1.Top = top + l1.Height;
                                    l1.Font = new Font("微软雅黑", 14);
                                    l1.ForeColor = Color.FromArgb(255, 51, 51, 51);
                                }
                                else
                                {
                                    l1.Name = "订单序号：" + jarry[i]["ordinal"].ToString();
                                    l1.Text = l1.Name;
                                    l1.AutoSize = true;
                                    l1.Left = left;
                                    l1.Top = top + l1.Height;
                                    l1.Font = new Font("微软雅黑", 14);
                                    l1.ForeColor = Color.FromArgb(255, 51, 51, 51);
                                }


                                Label lblTitle = new Label();
                                lblTitle.BackColor = Color.Transparent;
                                lblTitle.AutoSize = true;
                                if (jarry[i]["createTime"] == null)
                                {
                                   // MessageBox.Show("服务器异常：原始金额为空");
                                    lblTitle.Name = "";
                                }
                                else
                                {
                                    System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                                    DateTime dt = startTime.AddMilliseconds(long.Parse(jarry[i]["createTime"].ToString()));

                                    lblTitle.Name = "时间：" + dt.ToString("yyyy/MM/dd HH:mm:ss");
                                }


                                lblTitle.Text = lblTitle.Name;
                                lblTitle.Left = left;
                                lblTitle.Font = new Font("微软雅黑", 10);
                                lblTitle.ForeColor = Color.FromArgb(255, 51, 51, 51);
                                lblTitle.Top = 10 + lblTitle.Height + l1.Top+5;

                                
                                Label lblTitle3 = new Label();
                                lblTitle3.BackColor = Color.Transparent;
                                lblTitle3.AutoSize = true;
                                if (jarry[i]["primeMoney"] == null)
                                {
                                   // MessageBox.Show("服务器异常：原始金额为空");
                                    lblTitle3.Name = "金额";
                                }
                                else
                                {
                                    lblTitle3.Name = "金额：" + jarry[i]["primeMoney"].ToString();
                                }

                                lblTitle3.Font = new Font("微软雅黑", 10);
                                lblTitle3.ForeColor = Color.FromArgb(255, 51, 51, 51);
                                lblTitle3.Text = lblTitle3.Name;
                                lblTitle3.Left = left;
                                lblTitle3.Top = top + lblTitle3.Height + lblTitle.Top+5;


                                Button remove = new Button();
                                remove.FlatStyle = FlatStyle.Flat;
                                remove.FlatAppearance.BorderSize = 0;
                                remove.Size = new Size(15, 15);

                                //remove.Text = "✘";
                                remove.Font = new Font("宋体", 14);
                                remove.Location = new Point(174, 0);


                                ButtonChild pay = new ButtonChild();
                                pay.FlatStyle = FlatStyle.Flat;
                                pay.FlatAppearance.BorderSize = 0;
                                pay.Size = new Size(50, 20);
                                if (jarry[i]["primeMoney"] != null)
                                {
                                    pay.handlePrice = double.Parse(jarry[i]["primeMoney"].ToString());
                                }
                                if (jarry[i]["vipMoney"] != null)
                                {
                                    pay.reducedPrice = double.Parse(jarry[i]["vipMoney"].ToString());
                                }
                                if (jarry[i]["orderNO"] != null)
                                {
                                    pay.orderNo = jarry[i]["orderNO"].ToString();
                                    pay.orderId=int.Parse(jarry[i]["ID"].ToString());
                                }

                                if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) != 1)
                                {

                                    pay.BackgroundImage = Properties.Resources.nopay;
                                    pay.Click += new EventHandler(gathering);
                                    pay.Location = new Point(125, 108);


                                }

                                else if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                {
                                    pay.BackgroundImage = Properties.Resources.finish;
                                    pay.Size = new Size(66, 15);
                                    pay.Location = new Point(115, 108);
                                }


                                if (i >= 3 && i < 6)
                                {
                                    remove.BackgroundImage = Properties.Resources.remove2;
                                    PanelChild p = new PanelChild();
                                    p.Size = new Size(216, 135);
                                    p.BackColor = Color.FromArgb(255, 255, 255, 255);
                                   
                                    p.Location = new Point(1 + j * 216, 136);
                                    p.Controls.Add(l1);
                                   // p.Controls.Add(remove);
                                    p.Controls.Add(lblTitle);
                                    p.Controls.Add(lblTitle3);

                                    p.location = i;
                                    p.Controls.Add(pay);
                                    j++;

                                    o.orderPanel.Controls.Add(p);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                    if (jarry[i]["payType"] != null)
                                    {
                                        p.payType = int.Parse(jarry[i]["payType"].ToString());
                                    }
                                    if (jarry[i]["payNO"] != null)
                                    {
                                        p.payNO = jarry[i]["payNO"].ToString();
                                    }
                                    if (jarry[i]["primeMoney"] != null)
                                    {
                                        p.primeMoney = double.Parse(jarry[i]["primeMoney"].ToString());

                                    }
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        p.saleMoney = double.Parse(jarry[i]["saleMoney"].ToString());
                                    }
                                    if (jarry[i]["paidMoney"] != null)
                                    {
                                        p.paidMoney = double.Parse(jarry[i]["paidMoney"].ToString());
                                        pay.realityPrice = double.Parse(jarry[i]["paidMoney"].ToString());
                                    }
                                    if (jarry[i]["vipMoney"] != null)
                                    {
                                        p.memberMoney = double.Parse(jarry[i]["vipMoney"].ToString());
                                    }
                                    if (jarry[i]["serviceStatus"] != null)
                                    {
                                        p.serviceStatus = int.Parse(jarry[i]["serviceStatus"].ToString());
                                        if (int.Parse(jarry[i]["serviceStatus"].ToString())==1)
                                        {
                                            p.BackgroundImage = Properties.Resources.servicefinsh;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                        else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                        {
                                            pay.Visible = false;
                                            p.BackgroundImage = Properties.Resources.orderCancel;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                    }
                                    if (jarry[i]["payStatus"] != null)
                                    {
                                        p.payStatus = int.Parse(jarry[i]["payStatus"].ToString());
                                    }
                                    if (jarry[i]["deskNO"] == null)
                                    {
                                        p.ordinal = jarry[i]["ordinal"].ToString();
                                    }
                                    else
                                    {
                                        p.deskNO = jarry[i]["deskNO"].ToString();
                                        p.ordinal = jarry[i]["ordinal"].ToString();
                                        p.deskName = jarry[i]["deskName"].ToString();
                                    }

                                    p.Click += new EventHandler(showDeatail);
                                }
                                else if (i >= 6)
                                {
                                    remove.BackgroundImage = Properties.Resources.remove;
                                    PanelChild p = new PanelChild();
                                    p.Size = new Size(216, 135);
                                    p.BackColor = Color.FromArgb(255, 221, 221, 221);
                                    p.Location = new Point(1 + k * 216, 272);
                                    p.Controls.Add(l1);
                                   // p.Controls.Add(remove);
                                    p.location = i;
                                    p.Controls.Add(lblTitle);
                                    p.Controls.Add(lblTitle3);

                                    p.location = i;
                                    p.Controls.Add(pay);
                                    k++;

                                    o.orderPanel.Controls.Add(p);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                    if (jarry[i]["payType"] != null)
                                    {
                                        p.payType = int.Parse(jarry[i]["payType"].ToString());
                                    }
                                    if (jarry[i]["payNO"] != null)
                                    {
                                        p.payNO = jarry[i]["payNO"].ToString();
                                    }
                                    if (jarry[i]["primeMoney"] != null)
                                    {
                                        p.primeMoney = double.Parse(jarry[i]["primeMoney"].ToString());
                                    }
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        p.saleMoney = double.Parse(jarry[i]["saleMoney"].ToString());
                                    }
                                    if (jarry[i]["paidMoney"] != null)
                                    {
                                        p.paidMoney = double.Parse(jarry[i]["paidMoney"].ToString());
                                        pay.realityPrice = double.Parse(jarry[i]["paidMoney"].ToString());
                                    }
                                    if (jarry[i]["vipMoney"] != null)
                                    {
                                        p.memberMoney = double.Parse(jarry[i]["vipMoney"].ToString());
                                    }
                                    if (jarry[i]["serviceStatus"] != null)
                                    {
                                        p.serviceStatus = int.Parse(jarry[i]["serviceStatus"].ToString());
                                        if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 1)
                                        {
                                            p.BackgroundImage = Properties.Resources.servicefinsh;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                        else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                        {
                                            p.BackgroundImage = Properties.Resources.orderCancel;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
                                            pay.Visible = false;
                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                    }
                                    if (jarry[i]["payStatus"] != null)
                                    {
                                        p.payStatus = int.Parse(jarry[i]["payStatus"].ToString());
                                    }
                                    if (jarry[i]["deskNO"] == null)
                                    {
                                        p.ordinal = jarry[i]["ordinal"].ToString();
                                    }
                                    else
                                    {
                                        p.deskNO = jarry[i]["deskNO"].ToString();
                                        p.ordinal = jarry[i]["ordinal"].ToString();
                                        p.deskName = jarry[i]["deskName"].ToString();
                                    }
                                    p.Click += new EventHandler(showDeatail);
                                }
                                else
                                {
                                    remove.BackgroundImage = Properties.Resources.remove;
                                    PanelChild p = new PanelChild();
                                    p.Size = new Size(216, 135);
                                    p.BackColor = Color.FromArgb(255, 221, 221, 221);
                                    p.Location = new Point(1 + i * 216, 0);
                                    p.Controls.Add(l1);
                                 //   p.Controls.Add(remove);
                                    p.Controls.Add(lblTitle);
                                    p.Controls.Add(lblTitle3);

                                    p.Controls.Add(pay);

                                    o.orderPanel.Controls.Add(p);

                                    p.orderNO = jarry[i]["orderNO"].ToString();
                                    p.ID = int.Parse(jarry[i]["ID"].ToString());
                                    p.createTime = long.Parse(jarry[i]["createTime"].ToString());

                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                    if (jarry[i]["payType"] != null)
                                    {
                                        p.payType = int.Parse(jarry[i]["payType"].ToString());
                                    }
                                    if (jarry[i]["payNO"] != null)
                                    {
                                        p.payNO = jarry[i]["payNO"].ToString();
                                    }
                                    if (jarry[i]["primeMoney"] != null)
                                    {
                                        p.primeMoney = double.Parse(jarry[i]["primeMoney"].ToString());

                                    }
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        p.saleMoney = double.Parse(jarry[i]["saleMoney"].ToString());
                                    }
                                    if (jarry[i]["paidMoney"] != null)
                                    {
                                        p.paidMoney = double.Parse(jarry[i]["paidMoney"].ToString());
                                        pay.realityPrice = double.Parse(jarry[i]["paidMoney"].ToString());
                                    }
                                    if (jarry[i]["vipMoney"] != null)
                                    {
                                        p.memberMoney = double.Parse(jarry[i]["vipMoney"].ToString());
                                    }
                                    if (jarry[i]["serviceStatus"] != null)
                                    {
                                        p.serviceStatus = int.Parse(jarry[i]["serviceStatus"].ToString());
                                        if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 1)
                                        {
                                            p.BackgroundImage = Properties.Resources.servicefinsh;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                        else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                        {
                                            p.BackgroundImage = Properties.Resources.orderCancel;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
                                            pay.Visible = false;
                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                    }
                                    if (jarry[i]["payStatus"] != null)
                                    {
                                        p.payStatus = int.Parse(jarry[i]["payStatus"].ToString());
                                    }
                                    if (jarry[i]["deskNO"] == null)
                                    {
                                        p.ordinal = jarry[i]["ordinal"].ToString();
                                    }
                                    else
                                    {
                                        p.deskNO = jarry[i]["deskNO"].ToString();
                                        p.ordinal = jarry[i]["ordinal"].ToString();
                                        p.deskName = jarry[i]["deskName"].ToString();
                                    }
                                    p.Click += new EventHandler(showDeatail);
                                }
                            }
                        }
                        else
                        {
                            o.orderPanel.Controls.Clear();
                            o.page.Text = "0";
                            o.pageIndex.Text = "0";
                            o.count.Text = "0";
                            MessageBox.Show("暂无订单");
                        }


                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());

                    }
                }

            }


        }
        public static void printOrderDetail(object sender, EventArgs e)
        {
            try
            {
                ButtonChild p = (ButtonChild)sender;

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 91 }, { "muuid", Login.userInfo.muuid } };
                JObject json3 = new JObject { { "orderNO", p.orderNo } };

                json2.Add("data", json3.ToString());

                string result = string.Empty;
                result = HttpPostData(testurl.ToString(), json2.ToString());
                Console.WriteLine(json2.ToString());
                printPayAuto(result,"0");
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        public static void printAginOrderDetail(object sender, EventArgs e)
        {
            try
            {
                ButtonChild p = (ButtonChild)sender;

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 91 }, { "muuid", Login.userInfo.muuid } };
                JObject json3 = new JObject { { "orderNO", p.orderNo } };

                json2.Add("data", json3.ToString());

                string result = string.Empty;
                result = HttpPostData(testurl.ToString(), json2.ToString());
                printAginCp(result, "0");
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        public static void printOrderDetail(string orderNo)
        {
            try
            {
                string[] arr=orderNo.Split(',');
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 91 }, { "muuid", Login.userInfo.muuid } };
                JObject json3 = new JObject { { "orderNO", arr[0] } };

                json2.Add("data", json3.ToString());

                string result = string.Empty;
                result = HttpPostData(testurl.ToString(), json2.ToString());
                
                printPayAuto(result, arr[1]);
               
                        
               
            } catch (Exception q) { MessageBox.Show(q.Message); }


        }

        public static void printPayCp(string result, double balanceMoney, double cashMoney,string yh)
        {
            try {
            if (result != string.Empty)
            {
                Console.WriteLine("print" + result);
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                if (jo["errorCode"].ToString().Equals("0"))
                {
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                    if (jsonObect["printers"] == null)
                    {
                        MessageBox.Show("未设置打印机");
                    }
                    else
                    {
                        OrderInfo orderInfo = new OrderInfo();
                        orderInfo.ID = int.Parse(jsonObect["ID"].ToString());
                        orderInfo.orderNO = jsonObect["orderNO"].ToString();
                        orderInfo.balanceMoney = balanceMoney;
                        orderInfo.cashMoney = cashMoney;
                        orderInfo.yh = yh;
                        if (jsonObect["payNO"]!=null)
                        {
                            orderInfo.payNO = jsonObect["payNO"].ToString();
                        }
                            if (jsonObect["totalNum"] != null && int.Parse(jsonObect["totalNum"].ToString()) != 0)
                            {
                                orderInfo.peocount = jsonObect["totalNum"].ToString();
                            }
                            else
                            {
                                orderInfo.peocount = "未填";
                            }
                            orderInfo.createTime = long.Parse(jsonObect["createTime"].ToString());
                        orderInfo.payStatus = int.Parse(jsonObect["payStatus"].ToString());
                        if (jsonObect["deskName"] != null)
                        {
                            orderInfo.deskName = jsonObect["deskName"].ToString();
                        }
                        if (jsonObect["primeMoney"] != null)
                        {
                            orderInfo.primeMoney = double.Parse(jsonObect["primeMoney"].ToString());
                        }
                        if (jsonObect["saleMoney"] != null)
                        {
                            orderInfo.saleMoney = double.Parse(jsonObect["saleMoney"].ToString());
                        }

                        if (jsonObect["paidMoney"] != null)
                        {
                            orderInfo.paidMoney = double.Parse(jsonObect["paidMoney"].ToString());
                        }
                        if (jsonObect["vipMoney"] != null)
                        {
                            orderInfo.memberMoney = double.Parse(jsonObect["vipMoney"].ToString());
                        }

                        if (jsonObect["deskNO"] != null)
                        {
                            orderInfo.deskName = jsonObect["deskName"].ToString();
                            orderInfo.deskNO = jsonObect["deskNO"].ToString();
                        }
                        if (jsonObect["remark"] != null)
                        {
                            orderInfo.remark = jsonObect["remark"].ToString();
                        }
                        if (jsonObect["payType"]!=null) {
                            orderInfo.payType = int.Parse(jsonObect["payType"].ToString());
                        }
                        orderInfo.serviceStatus = int.Parse(jsonObect["serviceStatus"].ToString());
                        orderInfo.ordinal = int.Parse(jsonObect["ordinal"].ToString());
                        if (jsonObect["payURL"] != null)
                        {
                            orderInfo.payURL = jsonObect["payURL"].ToString();
                        }

                        if (jsonObect["payTime"] != null)
                        {
                            orderInfo.payTime = long.Parse(jsonObect["payTime"].ToString());
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

                        List<Cp> cps = new List<Cp>();
                        List<Cp> cpChilds = new List<Cp>();
                        if (jsonObect["attached"] != null)
                        {
                            JArray cpArray = JArray.Parse(jsonObect["attached"].ToString());

                            for (int i = 0; i < cpArray.Count; i++)
                            {
                                Cp cp = new Cp();
                                cp.id = int.Parse(cpArray[i]["ID"].ToString());
                                cp.name = cpArray[i]["name"].ToString();
                                cp.count = int.Parse(cpArray[i]["count"].ToString());
                                cp.price = double.Parse(cpArray[i]["price"].ToString());
                                cp.ODID = int.Parse(cpArray[i]["ODID"].ToString());
                                if (cpArray[i]["delFlag"] != null)
                                {
                                    cp.flag = int.Parse(cpArray[i]["delFlag"].ToString());
                                }
                                else
                                {
                                    cp.flag = 0;
                                }
                                if (cpArray[i]["vipPrice"] != null)
                                {
                                    cp.memberPrice = double.Parse(cpArray[i]["vipPrice"].ToString());
                                }

                                cp.printID = int.Parse(cpArray[i]["printID"].ToString());

                                if (cpArray[i]["attrs"] != null)
                                {
                                    JArray attrArray = JArray.Parse(cpArray[i]["attrs"].ToString());
                                    List<CpAttribution> attrs = new List<CpAttribution>();
                                    for (int j = 0; j < attrArray.Count; j++)
                                    {
                                        CpAttribution attr = new CpAttribution();
                                        attr.id = int.Parse(attrArray[j]["ID"].ToString());
                                        attr.text = attrArray[j]["text"].ToString();
                                        attrs.Add(attr);
                                    }
                                    cp.attrs = attrs;
                                }
                                if (cpArray[i]["subItems"] != null)
                                {
                                    cp.subItems = cpArray[i]["subItems"].ToString();
                                    cpChilds.Add(cp);
                                }
                                else
                                {
                                    if (cpArray[i]["isSub"] != null)
                                    {
                                        cp.isSub = int.Parse(cpArray[i]["isSub"].ToString());
                                    }
                                    cps.Add(cp);
                                }


                            }


                            foreach (Cp cp in cpChilds)
                            {
                                List<Cp> ccc = new List<Cp>();
                                foreach (Cp cp2 in cps)
                                {
                                    if (cp.ODID == cp2.ODID)
                                    {
                                            if (ccc.Count > 0)
                                            {
                                                if (!ccc.Exists(o => o.id == cp2.id))
                                                {
                                                    ccc.Add(cp2);
                                                }
                                            }
                                            else
                                            {
                                                ccc.Add(cp2);
                                            }
                                        }

                                }
                                cp.cpChild = ccc;
                            }

                            foreach (Cp cp in cps)
                            {
                                if (cp.isSub == 0)
                                {
                                    cpChilds.Add(cp);
                                }

                            }

                            //打印预览            
                            PrintPreviewDialog ppd = new PrintPreviewDialog();
                            for (int i = 0; i < prints.Count; i++)
                            {
                                List<Cp> printCps = new List<Cp>();
                                for (int j = 0; j < cpChilds.Count; j++)
                                {
                                    if (cpChilds[j].printID == prints[i].id)
                                    {
                                        printCps.Add(cpChilds[j]);
                                    }
                                }
                                PrintDocumentChild pd = new PrintDocumentChild();
                                pd.orderInfo = orderInfo;
                                pd.print = prints[i];
                                pd.print.cps = printCps;
                                pd.PrinterSettings.PrinterName = prints[i].name;

                                //设置边距

                                Margins margin = new Margins(20, 20, 20, 20);

                                pd.DefaultPageSettings.Margins = margin;


                                ////纸张设置默认

                                PaperSize pageSize = new PaperSize("First custom size", getYc(80), int.MaxValue);

                                pd.DefaultPageSettings.PaperSize = pageSize;



                                //打印事件设置            

                                pd.PrintPage += new PrintPageEventHandler(GetPrintStrTest);

                                ppd.Document = pd;
                                if (prints[i].isPay != 0)
                                {
                                    pd.Print();
                                }

                            }

                        }
                        else
                        {
                            MessageBox.Show("服务器异常：无菜品数据");
                        }

                    }

                }
                else
                {
                    MessageBox.Show(jo["message"].ToString());
                }


            }
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        public static void printAginCp(string result,string auto)
        {
            try
            {
                if (result != string.Empty)
                {
                    Console.WriteLine("print" + result);
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                        if (jsonObect["printers"] == null)
                        {
                            MessageBox.Show("未设置打印机");
                        }
                        else
                        {
                            OrderInfo orderInfo = new OrderInfo();
                            orderInfo.ID = int.Parse(jsonObect["ID"].ToString());
                            orderInfo.orderNO = jsonObect["orderNO"].ToString();
                            if (jsonObect["payNO"] != null)
                            {
                                orderInfo.payNO = jsonObect["payNO"].ToString();
                            }
                            if (jsonObect["totalNum"] != null && int.Parse(jsonObect["totalNum"].ToString()) != 0)
                            {
                                orderInfo.peocount = jsonObect["totalNum"].ToString();
                            }
                            else
                            {
                                orderInfo.peocount = "未填";
                            }
                            orderInfo.createTime = long.Parse(jsonObect["createTime"].ToString());
                            orderInfo.payStatus = int.Parse(jsonObect["payStatus"].ToString());
                            if (jsonObect["deskName"] != null)
                            {
                                orderInfo.deskName = jsonObect["deskName"].ToString();
                            }
                            if (jsonObect["primeMoney"] != null)
                            {
                                orderInfo.primeMoney = double.Parse(jsonObect["primeMoney"].ToString());
                            }
                            if (jsonObect["saleMoney"] != null)
                            {
                                orderInfo.saleMoney = double.Parse(jsonObect["saleMoney"].ToString());
                            }
                            orderInfo.yh = "0";
                            if (jsonObect["paidMoney"] != null)
                            {
                                
                                orderInfo.paidMoney = double.Parse(jsonObect["paidMoney"].ToString());
                                orderInfo.yh = (double.Parse(jsonObect["primeMoney"].ToString()) - double.Parse(jsonObect["paidMoney"].ToString())).ToString();
                            }
                            if (jsonObect["vipMoney"] != null)
                            {
                                orderInfo.memberMoney = double.Parse(jsonObect["vipMoney"].ToString());
                            }

                            if (jsonObect["deskNO"] != null)
                            {
                                orderInfo.deskName = jsonObect["deskName"].ToString();
                                orderInfo.deskNO = jsonObect["deskNO"].ToString();
                            }
                            if (jsonObect["remark"] != null)
                            {
                                orderInfo.remark = jsonObect["remark"].ToString();
                            }
                            if (jsonObect["payType"] != null)
                            {
                                orderInfo.payType = int.Parse(jsonObect["payType"].ToString());
                            }
                            orderInfo.serviceStatus = int.Parse(jsonObect["serviceStatus"].ToString());
                            orderInfo.ordinal = int.Parse(jsonObect["ordinal"].ToString());
                            if (jsonObect["payURL"] != null)
                            {
                                orderInfo.payURL = jsonObect["payURL"].ToString();
                            }

                            if (jsonObect["payTime"] != null)
                            {
                                orderInfo.payTime = long.Parse(jsonObect["payTime"].ToString());
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
                                    print.autoPrint = int.Parse(printArray[i]["autoPrint"].ToString());
                                    print.flag = int.Parse(printArray[i]["flag"].ToString());
                                    print.isFull = int.Parse(printArray[i]["isFull"].ToString());
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
                                return;
                            }

                            List<Cp> cps = new List<Cp>();
                            List<Cp> cpChilds = new List<Cp>();
                            if (jsonObect["attached"] != null)
                            {
                                JArray cpArray = JArray.Parse(jsonObect["attached"].ToString());

                                for (int i = 0; i < cpArray.Count; i++)
                                {
                                    Cp cp = new Cp();
                                    cp.id = int.Parse(cpArray[i]["ID"].ToString());
                                    cp.name = cpArray[i]["name"].ToString();
                                    cp.count = int.Parse(cpArray[i]["count"].ToString());
                                    cp.price = double.Parse(cpArray[i]["price"].ToString());
                                    cp.ODID = int.Parse(cpArray[i]["ODID"].ToString());

                                    if (cpArray[i]["isPrint"] != null)
                                    {
                                        cp.isPrint = int.Parse(cpArray[i]["isPrint"].ToString());
                                    }
                                    if (cpArray[i]["remark"] != null)
                                    {
                                        cp.remark = cpArray[i]["remark"].ToString();
                                    }
                                    if (cpArray[i]["delFlag"] != null)
                                    {
                                        cp.flag = int.Parse(cpArray[i]["delFlag"].ToString());
                                    }
                                    else
                                    {
                                        cp.flag = 0;
                                    }
                                    if (cpArray[i]["vipPrice"] != null)
                                    {
                                        cp.memberPrice = double.Parse(cpArray[i]["vipPrice"].ToString());
                                    }

                                    cp.printID = int.Parse(cpArray[i]["printID"].ToString());

                                    if (cpArray[i]["attrs"] != null)
                                    {
                                        JArray attrArray = JArray.Parse(cpArray[i]["attrs"].ToString());
                                        List<CpAttribution> attrs = new List<CpAttribution>();
                                        for (int j = 0; j < attrArray.Count; j++)
                                        {
                                            CpAttribution attr = new CpAttribution();
                                            attr.id = int.Parse(attrArray[j]["ID"].ToString());
                                            attr.text = attrArray[j]["text"].ToString();
                                            attrs.Add(attr);
                                        }
                                        cp.attrs = attrs;
                                    }
                                    if (cpArray[i]["subItems"] != null)
                                    {
                                        cp.subItems = cpArray[i]["subItems"].ToString();
                                        cpChilds.Add(cp);
                                    }
                                    else
                                    {
                                        if (cpArray[i]["isSub"] != null)
                                        {
                                            cp.isSub = int.Parse(cpArray[i]["isSub"].ToString());
                                        }
                                        cps.Add(cp);
                                    }


                                }


                                foreach (Cp cp in cpChilds)
                                {
                                    List<Cp> ccc = new List<Cp>();
                                    foreach (Cp cp2 in cps)
                                    {
                                        if (cp.ODID == cp2.ODID)
                                        {
                                            if (ccc.Count > 0)
                                            {                                             
                                               if (!ccc.Exists(o => o.id == cp2.id))
                                                {
                                                    ccc.Add(cp2);
                                                }
                                            }
                                            else
                                            {
                                                ccc.Add(cp2);
                                            }
                                          
                                        }

                                    }
                                   
                                    cp.cpChild = ccc;
                                }

                                foreach (Cp cp in cps)
                                {
                                    if (cp.isSub == 0)
                                    {
                                        cpChilds.Add(cp);
                                    }

                                }

                                //打印预览            
                                PrintPreviewDialog ppd = new PrintPreviewDialog();
                                for (int i = 0; i < prints.Count; i++)
                                {
                                    List<Cp> printCps = new List<Cp>();
                                    for (int j = 0; j < cpChilds.Count; j++)
                                    {
                                        if (cpChilds[j].printID == prints[i].id)
                                        {
                                            printCps.Add(cpChilds[j]);
                                        }
                                    }
                                    PrintDocumentChild pd = new PrintDocumentChild();
                                    pd.orderInfo = orderInfo;
                                    pd.print = prints[i];
                                    pd.print.cps = printCps;
                                    pd.PrinterSettings.PrinterName = prints[i].name;

                                    //设置边距

                                    Margins margin = new Margins(20, 20, 20, 20);

                                    pd.DefaultPageSettings.Margins = margin;


                                    ////纸张设置默认

                                    PaperSize pageSize = new PaperSize("First custom size", getYc(80), int.MaxValue);

                                    pd.DefaultPageSettings.PaperSize = pageSize;

                                    ppd.Document = pd;
                                    if (auto.Equals("0"))
                                    {
                                        
                                            if (prints[i].isFull != 0)
                                            {
                                                pd.PrintPage += new PrintPageEventHandler(GetPrintStrTest);
                                                pd.Print();
                                            }
                                            else
                                            {
                                                for (int j = 0; j < cpChilds.Count; j++)
                                                {
                                                    if (cpChilds[j].printID == prints[i].id && cpChilds[j].subItems != null && cpChilds[j].flag == 0)
                                                    {
                                                        for (int m = 0; m < cpChilds[j].cpChild.Count; m++)
                                                        {
                                                            pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                                            pd.print.cp2 = cpChilds[j].cpChild[m];
                                                            pd.Print();
                                                        }
                                                    }
                                                    else
                                                    if (cpChilds[j].printID == prints[i].id && cpChilds[j].flag == 0)
                                                    {
                                                        pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                                        pd.print.cp2 = cpChilds[j];
                                                        pd.Print();

                                                    }
                                                }

                                            }

                                        
                                    }
                                    else
                                    {
                                        if (prints[i].autoPrint != 0)
                                        {
                                            if (prints[i].isFull != 0)
                                            {
                                                pd.PrintPage += new PrintPageEventHandler(GetPrintStrTest);
                                                pd.Print();
                                            }
                                            else
                                            {
                                                for (int j = 0; j < cpChilds.Count; j++)
                                                {
                                                    if (cpChilds[j].printID == prints[i].id && cpChilds[j].subItems != null && cpChilds[j].flag == 0)
                                                    {
                                                        for (int m = 0; m < cpChilds[j].cpChild.Count; m++)
                                                        {
                                                            pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                                            pd.print.cp2 = cpChilds[j].cpChild[m];
                                                            pd.Print();
                                                        }
                                                    }
                                                    else
                                                    if (cpChilds[j].printID == prints[i].id && cpChilds[j].flag == 0)
                                                    {
                                                        pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                                        pd.print.cp2 = cpChilds[j];
                                                        pd.Print();

                                                    }
                                                }

                                            }

                                        }
                                    }
                                    

                                }
                            
                            }
                            else
                            {
                                MessageBox.Show("无菜品数据");
                            }

                        }

                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                    }


                }
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }

        private static void printPayAuto(string result,string auto )
        {
            //  try
            //  {
            ArrayList list = new ArrayList();
            if (result != string.Empty)
                {
                    Console.WriteLine("print" + result);
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                        if (jsonObect["printers"] == null)
                        {
                            MessageBox.Show("未设置打印机");
                        }
                        else
                        {
                            OrderInfo orderInfo = new OrderInfo();
                            if (jsonObect["ID"]!=null)
                            {
                             orderInfo.ID = int.Parse(jsonObect["ID"].ToString());
                            }
                            if (jsonObect["orderNO"] != null)
                            {
                                orderInfo.orderNO = jsonObect["orderNO"].ToString();
                            }

                            if (jsonObect["payNO"] != null)
                            {
                                orderInfo.payNO = jsonObect["payNO"].ToString();
                            }
                            if (jsonObect["totalNum"] != null&&int.Parse(jsonObect["totalNum"].ToString())!=0)
                            {
                                orderInfo.peocount = jsonObect["totalNum"].ToString();
                            }
                            else
                            {
                                orderInfo.peocount = "未填";
                            }
                            if (jsonObect["createTime"] != null )
                            {
                                orderInfo.createTime = long.Parse(jsonObect["createTime"].ToString());
                            }
                        if (jsonObect["payStatus"] != null)
                        {
                            orderInfo.payStatus = int.Parse(jsonObect["payStatus"].ToString());
                        }
                      
                            if (jsonObect["deskName"] != null)
                            {
                                orderInfo.deskName = jsonObect["deskName"].ToString();
                            }
                            orderInfo.yh = "0";
                            if (jsonObect["primeMoney"] != null)
                            {
                                orderInfo.primeMoney = double.Parse(jsonObect["primeMoney"].ToString());
                               
                            }
                            if (jsonObect["saleMoney"] != null)
                            {
                                orderInfo.saleMoney = double.Parse(jsonObect["saleMoney"].ToString());
                            }

                            if (jsonObect["paidMoney"] != null)
                            {
                                orderInfo.paidMoney = double.Parse(jsonObect["paidMoney"].ToString());
                                orderInfo.yh = (double.Parse(jsonObect["primeMoney"].ToString()) - double.Parse(jsonObect["paidMoney"].ToString())).ToString(); 
                            }
                            if (jsonObect["vipMoney"] != null)
                            {
                                orderInfo.memberMoney = double.Parse(jsonObect["vipMoney"].ToString());
                            }

                            if (jsonObect["deskNO"] != null)
                            {
                                orderInfo.deskName = jsonObect["deskName"].ToString();
                                orderInfo.deskNO = jsonObect["deskNO"].ToString();
                            }
                            if (jsonObect["remark"] != null)
                            {
                                orderInfo.remark = jsonObect["remark"].ToString();
                            }
                            if (jsonObect["payType"] != null)
                            {
                                orderInfo.payType = int.Parse(jsonObect["payType"].ToString());
                            }
                        if (jsonObect["serviceStatus"] != null)
                        {
                            orderInfo.serviceStatus = int.Parse(jsonObect["serviceStatus"].ToString());
                        }
                        if (jsonObect["ordinal"] != null)
                        {
                            orderInfo.ordinal = int.Parse(jsonObect["ordinal"].ToString());
                        }
                    
                            if (jsonObect["payURL"] != null)
                            {
                                orderInfo.payURL = jsonObect["payURL"].ToString();
                            }

                            if (jsonObect["payTime"] != null)
                            {
                                orderInfo.payTime = long.Parse(jsonObect["payTime"].ToString());
                            }


                            List<Print> prints = new List<Print>();
                            if (jsonObect["printers"] != null)
                            {
                                JArray printArray = JArray.Parse(jsonObect["printers"].ToString());
                                for (int i = 0; i < printArray.Count; i++)
                                {
                                    Print print = new Print();
                                    if (printArray[i]["ID"] != null) {
                       
                                        print.id = int.Parse(printArray[i]["ID"].ToString());
                                    }
                                    if (printArray[i]["type"] != null)
                                    {
                                        print.type = int.Parse(printArray[i]["type"].ToString());
                                    }
                                    if (printArray[i]["name"] != null)
                                    {
                                        print.name = printArray[i]["name"].ToString();
                                    }
                                    if (printArray[i]["port"] != null)
                                    {
                                        print.port = printArray[i]["port"].ToString();
                                    }
                                    if (printArray[i]["autoPrint"] != null)
                                    {
                                        print.autoPrint = int.Parse(printArray[i]["autoPrint"].ToString());
                                    }
                                    if (printArray[i]["flag"] != null)
                                    {
                                        print.flag = int.Parse(printArray[i]["flag"].ToString());
                                    }
                                if (printArray[i]["isFull"] != null)
                                {
                                    print.isFull = int.Parse(printArray[i]["isFull"].ToString());
                                }
                               
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
                                return;
                            }

                            List<Cp> cps = new List<Cp>();
                            List<Cp> cpChilds = new List<Cp>();
                            if (jsonObect["attached"] != null)
                            {
                                JArray cpArray = JArray.Parse(jsonObect["attached"].ToString());

                                for (int i = 0; i < cpArray.Count; i++)
                                {
                                    Cp cp = new Cp();
                                    if (cpArray[i]["ID"]!=null)
                                    {
                                        cp.id = int.Parse(cpArray[i]["ID"].ToString());
                                    }
                                    if (cpArray[i]["name"] != null)
                                    {
                                        cp.name = cpArray[i]["name"].ToString();
                                    }
                                    if (cpArray[i]["count"] != null)
                                    {
                                        cp.count = int.Parse(cpArray[i]["count"].ToString());
                                    }
                                    if (cpArray[i]["price"] != null)
                                    {
                                        cp.price = double.Parse(cpArray[i]["price"].ToString());
                                    }
                                    if (cpArray[i]["ODID"] != null)
                                    {
                                        cp.ODID = int.Parse(cpArray[i]["ODID"].ToString());
                                    }
                               
                                    if (cpArray[i]["isPrint"] != null)
                                    {
                                        cp.isPrint = int.Parse(cpArray[i]["isPrint"].ToString());
                                    }
                                    if (cpArray[i]["remark"] != null)
                                    {
                                        cp.remark = cpArray[i]["remark"].ToString();
                                    }
                                    if (cpArray[i]["delFlag"] != null)
                                    {
                                        cp.flag = int.Parse(cpArray[i]["delFlag"].ToString());
                                    }
                                    else
                                    {
                                        cp.flag = 0;
                                    }
                                    if (cpArray[i]["vipPrice"] != null)
                                    {
                                        cp.memberPrice = double.Parse(cpArray[i]["vipPrice"].ToString());
                                    }
                                    if (cpArray[i]["printID"] != null)
                                    {
                                        cp.printID = int.Parse(cpArray[i]["printID"].ToString());
                                    }
                              

                                    if (cpArray[i]["attrs"] != null)
                                    {
                                        JArray attrArray = JArray.Parse(cpArray[i]["attrs"].ToString());
                                        List<CpAttribution> attrs = new List<CpAttribution>();
                                        for (int j = 0; j < attrArray.Count; j++)
                                        {
                                            CpAttribution attr = new CpAttribution();
                                            attr.id = int.Parse(attrArray[j]["ID"].ToString());
                                            attr.text = attrArray[j]["text"].ToString();
                                            attrs.Add(attr);
                                        }
                                        cp.attrs = attrs;
                                    }
                                    if (cpArray[i]["subItems"] != null)
                                    {
                                        cp.subItems = cpArray[i]["subItems"].ToString();
                                        cpChilds.Add(cp);
                                    }
                                    else
                                    {
                                        if (cpArray[i]["isSub"] != null)
                                        {
                                            cp.isSub = int.Parse(cpArray[i]["isSub"].ToString());
                                        }
                                        cps.Add(cp);
                                    }
                                    if (cpArray[i]["isRefundPrint"] != null)
                                    {
                                        cp.isRefundPrint = int.Parse(cpArray[i]["isRefundPrint"].ToString());
                                    }

                            }


                                foreach (Cp cp in cpChilds)
                                {
                                    List<Cp> ccc = new List<Cp>();
                                    foreach (Cp cp2 in cps)
                                    {
                                        if (cp.ODID == cp2.ODID)
                                        {
                                            if (ccc.Count > 0)
                                            {
                                                if (!ccc.Exists(o => o.id == cp2.id))
                                                {
                                                    ccc.Add(cp2);
                                                }
                                            }
                                            else
                                            {
                                                ccc.Add(cp2);
                                            }
                                        }

                                    }
                                    cp.cpChild = ccc;
                                }

                                foreach (Cp cp in cps)
                                {
                                    if (cp.isSub == 0)
                                    {
                                        cpChilds.Add(cp);
                                    }

                                }

                                //打印预览            
                                PrintPreviewDialog ppd = new PrintPreviewDialog();
                                for (int i = 0; i < prints.Count; i++)
                                {
                                    List<Cp> printCps = new List<Cp>();
                                    List<Cp> printCpsRefund = new List<Cp>();
                                    for (int j = 0; j < cpChilds.Count; j++)
                                    {
                                        if (cpChilds[j].printID == prints[i].id&& cpChilds[j].isPrint==0&& prints[i].isPay==0)
                                        {
                                            printCps.Add(cpChilds[j]);
                                        }else
                                        if (cpChilds[j].printID == prints[i].id && prints[i].isPay != 0)
                                        {
                                            printCps.Add(cpChilds[j]);
                                        }
                                        if (cpChilds[j].printID == prints[i].id && cpChilds[j].flag==-1 && cpChilds[j].isRefundPrint == 0 && prints[i].isPay == 0)
                                        {
                                            printCpsRefund.Add(cpChilds[j]);
                                          list.Add(cpChilds[j].ODID);
                                        }
                                     }
                                    PrintDocumentChild pd = new PrintDocumentChild();
                                    pd.orderInfo = orderInfo;
                                    pd.print = prints[i];
                                    pd.print.cps = printCps;
                                    pd.print.cpsRefund = printCpsRefund;
                                    
                                    pd.PrinterSettings.PrinterName = prints[i].name;

                                    //设置边距

                                    Margins margin = new Margins(20, 20, 20, 20);

                                    pd.DefaultPageSettings.Margins = margin;


                                    ////纸张设置默认

                                    PaperSize pageSize = new PaperSize("First custom size", getYc(80), int.MaxValue);

                                    pd.DefaultPageSettings.PaperSize = pageSize;

                                    ppd.Document = pd;
                                if (auto.Equals("0"))//手动打印
                                {
                                    
                                        if (prints[i].isFull != 0)
                                        {
                                            if (prints[i].isPay==0)
                                            {
                                                if (prints[i].cps.Count > 0)
                                                {
                                                    pd.PrintPage += new PrintPageEventHandler(GetPrintStrTest);
                                                    pd.Print();
                                                }
                                                if (prints[i].cpsRefund.Count > 0)
                                                {
                                                    pd.PrintPage += new PrintPageEventHandler(GetPrintStrRefund);
                                                    pd.Print();
                                                }
                                            } else
                                            {

                                                pd.PrintPage += new PrintPageEventHandler(GetPrintStrTest);
                                                pd.Print();
                                            }
                                            
                                        }
                                        else
                                        {
                                            for (int j = 0; j < cpChilds.Count; j++)
                                            {
                                                if (cpChilds[j].subItems != null && cpChilds[j].flag == 0 && cpChilds[j].isPrint == 0)
                                                {
                                                    for (int m = 0; m < cpChilds[j].cpChild.Count; m++)
                                                    {
                                                        if (cpChilds[j].cpChild[m].printID == prints[i].id)
                                                        {
                                                            pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                                            pd.print.cp2 = cpChilds[j].cpChild[m];
                                                            pd.Print();
                                                        }

                                                    }
                                                }
                                                else
                                                if (cpChilds[j].printID == prints[i].id && cpChilds[j].flag == 0 && cpChilds[j].isPrint == 0)
                                                {
                                                    pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                                    pd.print.cp2 = cpChilds[j];
                                                    pd.Print();

                                                }
                                            }

                                        }

                                    

                                }
                                else
                                {

                                    if (prints[i].autoPrint != 0)
                                    {
                                        if (prints[i].isFull != 0)
                                        {
                                            if (prints[i].cps.Count > 0)
                                            {
                                                pd.PrintPage += new PrintPageEventHandler(GetPrintStrTest);
                                                pd.Print();
                                            }
                                            if (prints[i].cpsRefund.Count > 0)
                                            {
                                                pd.PrintPage += new PrintPageEventHandler(GetPrintStrRefund);
                                                pd.Print();
                                            }
                                        }
                                        else
                                        {
                                            for (int j = 0; j < cpChilds.Count; j++)
                                            {
                                                if (cpChilds[j].subItems != null && cpChilds[j].flag == 0 && cpChilds[j].isPrint == 0)
                                                {
                                                    for (int m = 0; m < cpChilds[j].cpChild.Count; m++)
                                                    {
                                                        if (cpChilds[j].cpChild[m].printID == prints[i].id)
                                                        {
                                                            pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                                            pd.print.cp2 = cpChilds[j].cpChild[m];
                                                            pd.Print();
                                                        }

                                                    }
                                                }
                                                else
                                                if (cpChilds[j].printID == prints[i].id && cpChilds[j].flag == 0 && cpChilds[j].isPrint == 0)
                                                {
                                                    pd.PrintPage += new PrintPageEventHandler(GetPrintStr);
                                                    pd.print.cp2 = cpChilds[j];
                                                    pd.Print();

                                                }
                                            }

                                        }

                                    }

                                }

                            }
                            if (list.Count > 0)
                            {
                                string jsonData11 = JsonConvert.SerializeObject(list);
                                JArray attached = JArray.Parse(jsonData11);
                                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 96 }, { "muuid", Login.userInfo.muuid } };
                                JObject json3 = new JObject { { "ID", orderInfo.ID }, { "isPrint", 1 }, { "refundPrintIDs" , attached } };

                                json2.Add("data", json3.ToString());

                                string result2 = string.Empty;
                                result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                            }
                            else
                            {
                                string jsonData11 = JsonConvert.SerializeObject(list);
                                JArray attached = JArray.Parse(jsonData11);
                                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 96 }, { "muuid", Login.userInfo.muuid } };
                                JObject json3 = new JObject { { "ID", orderInfo.ID }, { "isPrint", 1 } };

                                json2.Add("data", json3.ToString());

                                string result2 = string.Empty;
                                result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                            }
                           
                            }
                            else
                            {
                                MessageBox.Show("服务器异常：无菜品数据");
                            }

                        }

                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                    }


                }
         //   }
         //   catch (Exception q) { MessageBox.Show(q.Message); }

        }
        private static void printRefundAuto(string result)
        {
            //  try
            //  {
            if (result != string.Empty)
            {
                Console.WriteLine("print" + result);
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                if (jo["errorCode"].ToString().Equals("0"))
                {
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                    if (jsonObect["printers"] == null)
                    {
                        MessageBox.Show("未设置打印机");
                    }
                    else
                    {
                        OrderInfo orderInfo = new OrderInfo();
                        if (jsonObect["ID"] != null)
                        {
                            orderInfo.ID = int.Parse(jsonObect["ID"].ToString());
                        }
                        if (jsonObect["orderNO"] != null)
                        {
                            orderInfo.orderNO = jsonObect["orderNO"].ToString();
                        }

                        if (jsonObect["payNO"] != null)
                        {
                            orderInfo.payNO = jsonObect["payNO"].ToString();
                        }
                        if (jsonObect["totalNum"] != null && int.Parse(jsonObect["totalNum"].ToString()) != 0)
                        {
                            orderInfo.peocount = jsonObect["totalNum"].ToString();
                        }
                        else
                        {
                            orderInfo.peocount = "未填";
                        }
                        if (jsonObect["createTime"] != null)
                        {
                            orderInfo.createTime = long.Parse(jsonObect["createTime"].ToString());
                        }
                        if (jsonObect["payStatus"] != null)
                        {
                            orderInfo.payStatus = int.Parse(jsonObect["payStatus"].ToString());
                        }

                        if (jsonObect["deskName"] != null)
                        {
                            orderInfo.deskName = jsonObect["deskName"].ToString();
                        }
                        orderInfo.yh = "0";
                        if (jsonObect["primeMoney"] != null)
                        {
                            orderInfo.primeMoney = double.Parse(jsonObect["primeMoney"].ToString());

                        }
                        if (jsonObect["saleMoney"] != null)
                        {
                            orderInfo.saleMoney = double.Parse(jsonObect["saleMoney"].ToString());
                        }

                        if (jsonObect["paidMoney"] != null)
                        {
                            orderInfo.paidMoney = double.Parse(jsonObect["paidMoney"].ToString());
                            orderInfo.yh = (double.Parse(jsonObect["primeMoney"].ToString()) - double.Parse(jsonObect["paidMoney"].ToString())).ToString();
                        }
                        if (jsonObect["vipMoney"] != null)
                        {
                            orderInfo.memberMoney = double.Parse(jsonObect["vipMoney"].ToString());
                        }

                        if (jsonObect["deskNO"] != null)
                        {
                            orderInfo.deskName = jsonObect["deskName"].ToString();
                            orderInfo.deskNO = jsonObect["deskNO"].ToString();
                        }
                        if (jsonObect["remark"] != null)
                        {
                            orderInfo.remark = jsonObect["remark"].ToString();
                        }
                        if (jsonObect["payType"] != null)
                        {
                            orderInfo.payType = int.Parse(jsonObect["payType"].ToString());
                        }
                        if (jsonObect["serviceStatus"] != null)
                        {
                            orderInfo.serviceStatus = int.Parse(jsonObect["serviceStatus"].ToString());
                        }
                        if (jsonObect["ordinal"] != null)
                        {
                            orderInfo.ordinal = int.Parse(jsonObect["ordinal"].ToString());
                        }

                        if (jsonObect["payURL"] != null)
                        {
                            orderInfo.payURL = jsonObect["payURL"].ToString();
                        }

                        if (jsonObect["payTime"] != null)
                        {
                            orderInfo.payTime = long.Parse(jsonObect["payTime"].ToString());
                        }


                        List<Print> prints = new List<Print>();
                        if (jsonObect["printers"] != null)
                        {
                            JArray printArray = JArray.Parse(jsonObect["printers"].ToString());
                            for (int i = 0; i < printArray.Count; i++)
                            {
                                Print print = new Print();
                                if (printArray[i]["ID"] != null)
                                {

                                    print.id = int.Parse(printArray[i]["ID"].ToString());
                                }
                                if (printArray[i]["type"] != null)
                                {
                                    print.type = int.Parse(printArray[i]["type"].ToString());
                                }
                                if (printArray[i]["name"] != null)
                                {
                                    print.name = printArray[i]["name"].ToString();
                                }
                                if (printArray[i]["port"] != null)
                                {
                                    print.port = printArray[i]["port"].ToString();
                                }
                                if (printArray[i]["autoPrint"] != null)
                                {
                                    print.autoPrint = int.Parse(printArray[i]["autoPrint"].ToString());
                                }
                                if (printArray[i]["flag"] != null)
                                {
                                    print.flag = int.Parse(printArray[i]["flag"].ToString());
                                }
                                if (printArray[i]["isFull"] != null)
                                {
                                    print.isFull = int.Parse(printArray[i]["isFull"].ToString());
                                }

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
                            return;
                        }

                        List<Cp> cps = new List<Cp>();
                        List<Cp> cpChilds = new List<Cp>();
                        if (jsonObect["attached"] != null)
                        {
                            JArray cpArray = JArray.Parse(jsonObect["attached"].ToString());

                            for (int i = 0; i < cpArray.Count; i++)
                            {
                                Cp cp = new Cp();
                                if (cpArray[i]["ID"] != null)
                                {
                                    cp.id = int.Parse(cpArray[i]["ID"].ToString());
                                }
                                if (cpArray[i]["name"] != null)
                                {
                                    cp.name = cpArray[i]["name"].ToString();
                                }
                                if (cpArray[i]["count"] != null)
                                {
                                    cp.count = int.Parse(cpArray[i]["count"].ToString());
                                }
                                if (cpArray[i]["price"] != null)
                                {
                                    cp.price = double.Parse(cpArray[i]["price"].ToString());
                                }
                                if (cpArray[i]["ODID"] != null)
                                {
                                    cp.ODID = int.Parse(cpArray[i]["ODID"].ToString());
                                }

                                if (cpArray[i]["isPrint"] != null)
                                {
                                    cp.isPrint = int.Parse(cpArray[i]["isPrint"].ToString());
                                }
                                if (cpArray[i]["remark"] != null)
                                {
                                    cp.remark = cpArray[i]["remark"].ToString();
                                }
                                if (cpArray[i]["delFlag"] != null)
                                {
                                    cp.flag = int.Parse(cpArray[i]["delFlag"].ToString());
                                }
                                else
                                {
                                    cp.flag = 0;
                                }
                                if (cpArray[i]["vipPrice"] != null)
                                {
                                    cp.memberPrice = double.Parse(cpArray[i]["vipPrice"].ToString());
                                }
                                if (cpArray[i]["printID"] != null)
                                {
                                    cp.printID = int.Parse(cpArray[i]["printID"].ToString());
                                }
                                if (cpArray[i]["isRefundPrint"] != null)
                                {
                                    cp.isRefundPrint = int.Parse(cpArray[i]["isRefundPrint"].ToString());
                                }

                                if (cpArray[i]["attrs"] != null)
                                {
                                    JArray attrArray = JArray.Parse(cpArray[i]["attrs"].ToString());
                                    List<CpAttribution> attrs = new List<CpAttribution>();
                                    for (int j = 0; j < attrArray.Count; j++)
                                    {
                                        CpAttribution attr = new CpAttribution();
                                        attr.id = int.Parse(attrArray[j]["ID"].ToString());
                                        attr.text = attrArray[j]["text"].ToString();
                                        attrs.Add(attr);
                                    }
                                    cp.attrs = attrs;
                                }
                                if (cpArray[i]["subItems"] != null)
                                {
                                    cp.subItems = cpArray[i]["subItems"].ToString();
                                    cpChilds.Add(cp);
                                }
                                else
                                {
                                    if (cpArray[i]["isSub"] != null)
                                    {
                                        cp.isSub = int.Parse(cpArray[i]["isSub"].ToString());
                                    }
                                    cps.Add(cp);
                                }


                            }


                            foreach (Cp cp in cpChilds)
                            {
                                List<Cp> ccc = new List<Cp>();
                                foreach (Cp cp2 in cps)
                                {
                                    if (cp.ODID == cp2.ODID)
                                    {
                                        if (ccc.Count > 0)
                                        {
                                            if (!ccc.Exists(o => o.id == cp2.id))
                                            {
                                                ccc.Add(cp2);
                                            }
                                        }
                                        else
                                        {
                                            ccc.Add(cp2);
                                        }
                                    }

                                }
                                cp.cpChild = ccc;
                            }

                            foreach (Cp cp in cps)
                            {
                                if (cp.isSub == 0)
                                {
                                    cpChilds.Add(cp);
                                }

                            }

                            //打印预览            
                            PrintPreviewDialog ppd = new PrintPreviewDialog();
                            for (int i = 0; i < prints.Count; i++)
                            {
                                List<Cp> printCps = new List<Cp>();
                                for (int j = 0; j < cpChilds.Count; j++)
                                {
                                    if (cpChilds[j].printID == prints[i].id && cpChilds[j].isRefundPrint == 0&& cpChilds[j].flag == 1)
                                    {
                                        printCps.Add(cpChilds[j]);
                                    }
                                  
                                }
                                PrintDocumentChild pd = new PrintDocumentChild();
                                pd.orderInfo = orderInfo;
                                pd.print = prints[i];
                                pd.print.cps = printCps;
                                pd.PrinterSettings.PrinterName = prints[i].name;

                                //设置边距

                                Margins margin = new Margins(20, 20, 20, 20);

                                pd.DefaultPageSettings.Margins = margin;


                                ////纸张设置默认

                                PaperSize pageSize = new PaperSize("First custom size", getYc(80), int.MaxValue);

                                pd.DefaultPageSettings.PaperSize = pageSize;

                                ppd.Document = pd;
                               
                                    if (prints[i].autoPrint != 0)
                                    {
                                        if (prints[i].isFull != 0)
                                        {
                                            if (prints[i].cps.Count > 0)
                                            {
                                                pd.PrintPage += new PrintPageEventHandler(GetPrintStrRefund);
                                                pd.Print();
                                            }
                                        }
                                        else
                                        {
                                            for (int j = 0; j < cpChilds.Count; j++)
                                            {
                                                if (cpChilds[j].subItems != null && cpChilds[j].flag == 1 && cpChilds[j].isRefundPrint == 0)
                                                {
                                                    for (int m = 0; m < cpChilds[j].cpChild.Count; m++)
                                                    {
                                                        if (cpChilds[j].cpChild[m].printID == prints[i].id)
                                                        {
                                                            pd.PrintPage += new PrintPageEventHandler(GetPrintStrRefund2);
                                                            pd.print.cp2 = cpChilds[j].cpChild[m];
                                                            pd.Print();
                                                        }

                                                    }
                                                }
                                                else
                                                if (cpChilds[j].printID == prints[i].id && cpChilds[j].flag == 1 && cpChilds[j].isRefundPrint == 0)
                                                {
                                                    pd.PrintPage += new PrintPageEventHandler(GetPrintStrRefund2);
                                                    pd.print.cp2 = cpChilds[j];
                                                    pd.Print();

                                                }
                                            }

                                        }

                                    }

                                }

                            
                            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 96 }, { "muuid", Login.userInfo.muuid } };
                            JObject json3 = new JObject { { "ID", orderInfo.ID }, { "isPrint", 1 } };

                            json2.Add("data", json3.ToString());

                            string result2 = string.Empty;
                            result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                            if (result2 != string.Empty)
                            {
                                Console.WriteLine(result2);
                            }
                        }
                        else
                        {
                            MessageBox.Show("服务器异常：无菜品数据");
                        }

                    }

                }
                else
                {
                    MessageBox.Show(jo["message"].ToString());
                }


            }
            //   }
            //   catch (Exception q) { MessageBox.Show(q.Message); }

        }
        private static int getYc(double cm)

        {

            return (int)(cm / 25.4) * 100;

        }




        public static void GetPrintStr(object sender, PrintPageEventArgs e)
        {

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
            type.Text = printDocumentChild.print.name;
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
            Label odinal = new Label();
            odinal.Text = "订单序号: " + printDocumentChild.orderInfo.ordinal;
            odinal.Left = left;
            odinal.Font = new Font("微软雅黑", 10);
            odinal.ForeColor = Color.Gray;

            if (printDocumentChild.orderInfo.deskName != null)
            {
                dNumber.Text = "桌台: " + printDocumentChild.orderInfo.deskName;
                dNumber.Left = left;
                dNumber.Font = new Font("微软雅黑", 10);
                dNumber.ForeColor = Color.Gray;
                dNumber.Top = top + dNumber.Height + u.Top - 3;
                odinal.Top = top + odinal.Height + dNumber.Top - 3;
                p.Controls.Add(dNumber);
            }
            else
            {
                odinal.Top = top + odinal.Height + u.Top - 3;
            }

            Label peocount = new Label();
            peocount.Text = "人数: " + printDocumentChild.orderInfo.peocount;
            peocount.Left = left + 150;
            peocount.Font = new Font("微软雅黑", 10);
            peocount.ForeColor = Color.Gray;
            peocount.Top = top + dNumber.Height + u.Top - 3;
            p.Controls.Add(peocount);

            Label printDate = new Label();
            printDate.Text = "打印日期: " + DateTime.Now.ToShortDateString();
            printDate.Left = left;
            printDate.Font = new Font("微软雅黑", 10);
            printDate.ForeColor = Color.Gray;
            printDate.Top = top + printDate.Height + odinal.Top - 3;

            Label orderNo = new Label();
            orderNo.Text = "单号: " + printDocumentChild.orderInfo.orderNO;
            orderNo.Left = left;
            orderNo.Font = new Font("微软雅黑", 10);
            orderNo.ForeColor = Color.Gray;
            orderNo.Top = top + orderNo.Height + printDate.Top - 3;


            System.DateTime orderTIME = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
            DateTime dt = orderTIME.AddMilliseconds(printDocumentChild.orderInfo.createTime);
            Label orderDate = new Label();
            orderDate.Text = "下单时间: " + dt.ToString("yyyy/MM/dd HH:mm:ss");
            orderDate.Left = left;
            orderDate.Font = new Font("微软雅黑", 10);
            orderDate.ForeColor = Color.Gray;
            orderDate.Top = top + orderDate.Height + orderNo.Top - 3;

            Label u2 = new Label();
            u2.Text = "--------------------------------------------";
            u2.Left = left;
            u2.Font = new Font("微软雅黑", 10);
            u2.ForeColor = Color.Gray;
            u2.Top = top + u2.Height + orderDate.Top - 3;

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
            cpPrice.Text = "单价";
            cpPrice.Left = left + 230;
            cpPrice.Font = new Font("微软雅黑", 10);
            cpPrice.ForeColor = Color.Gray;
            cpPrice.Top = top + cpName.Height + u2.Top;
            p.Controls.Add(cpName);
            p.Controls.Add(cpCount);
            p.Controls.Add(cpPrice);
            int t = 0;
            int l = cpPrice.Top;
           // for (int i = 0; i < printDocumentChild.print.cps.Count; i++)

           // {
                Label name = new Label();
                name.Left = left;
                name.ForeColor = Color.Gray;

                Label count = new Label();
                count.Left = left + 150;

                count.ForeColor = Color.Gray;

                Label price = new Label();
                price.Left = left + 235;

                price.ForeColor = Color.Gray;

                name.Top = top + name.Height + l;


                count.Top = top + count.Height + l;


                price.Top = top + price.Height + l;

                t = price.Top;

                if (printDocumentChild.print.cp2.attrs != null)
                {
                    string attrs = "";
                    for (int j = 0; j < printDocumentChild.print.cp2.attrs.Count; j++)
                    {
                        if (printDocumentChild.print.cp2.attrs.Count - 1 == j)
                        {
                            attrs += printDocumentChild.print.cp2.attrs[j].text;
                        }
                        else
                        {
                            attrs += printDocumentChild.print.cp2.attrs[j].text + ",";
                        }

                    }

                    name.Text = printDocumentChild.print.cp2.name + "(" + attrs + ")";

                    count.Text = printDocumentChild.print.cp2.count.ToString();
                    price.Text = printDocumentChild.print.cp2.price.ToString();

                }
                else
                {
                    name.Text = printDocumentChild.print.cp2.name;

                    count.Text = printDocumentChild.print.cp2.count.ToString();
                    price.Text = printDocumentChild.print.cp2.price.ToString();

                }
                if (printDocumentChild.print.cp2.flag == -1)
                {
                name.Text += "(退)";
                name.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    price.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    count.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                }
                else
                {
                    price.Font = new Font("微软雅黑", 10);
                    name.Font = new Font("微软雅黑", 10);
                    count.Font = new Font("微软雅黑", 10);
                }
                p.Controls.Add(name);
                p.Controls.Add(count);
                p.Controls.Add(price);

          //  }

            Label u3 = new Label();
            u3.Text = "--------------------------------------------";
            u3.Left = left;
            u3.Font = new Font("微软雅黑", 10);
            u3.ForeColor = Color.Gray;
            u3.Top = top + u3.Height + t;
            p.Controls.Add(u3);

       
            Label primeMoney = new Label();
            primeMoney.Text = "单价: ￥" + printDocumentChild.print.cp2.price;
            primeMoney.Left = left;
            primeMoney.Font = new Font("微软雅黑", 10);
            primeMoney.ForeColor = Color.Gray;
            primeMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(primeMoney);

            Label memberMoney = new Label();
            memberMoney.Text = "会员价: ￥" + printDocumentChild.print.cp2.memberPrice;
            memberMoney.Left = left + 150;
            memberMoney.Font = new Font("微软雅黑", 10);
            memberMoney.ForeColor = Color.Gray;
            memberMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(memberMoney);

            Label remark = new Label();
            remark.Text = "订单备注: " + printDocumentChild.orderInfo.remark;
            remark.Left = left;
            remark.Font = new Font("微软雅黑", 10);
            remark.ForeColor = Color.Gray;
            remark.Top = top + remark.Height + memberMoney.Top;
            p.Controls.Add(remark);

            Label cpremark = new Label();
            cpremark.AutoSize = false;

            cpremark.Text = "菜品备注: " + printDocumentChild.print.cp2.remark;        
            int jd = cpremark.Text.Length / 20;
            if (cpremark.Text.Length != 20 && cpremark.Text.Length > 20)
            {
                for (int m = 1; m <= jd; m++)
                {
                    if (m != 1)
                    {
                        cpremark.Text = cpremark.Text.Insert(m * 20 + 1, "\r\n");
                    }
                    else
                    {
                        cpremark.Text = cpremark.Text.Insert(m * 20, "\r\n");
                    }

                }
            }
           //if (cpremark.Text.Length>21)
           // {
           //     cpremark.Text= cpremark.Text.Insert(21,"\r\n");              
           // }
            cpremark.Left = left;
            cpremark.Font = new Font("微软雅黑", 10);
            cpremark.ForeColor = Color.Gray;
            cpremark.Top = top + cpremark.Height + remark.Top;
            p.Controls.Add(cpremark);

            p.Controls.Add(shopName);
            p.Controls.Add(type);
            p.Controls.Add(u);
            p.Controls.Add(dNumber);
            p.Controls.Add(odinal);
            p.Controls.Add(printDate);
            p.Controls.Add(orderNo);
            p.Controls.Add(orderDate);
            p.Controls.Add(u2);


            foreach (Control c in p.Controls)
            {
                if (c.GetType() == typeof(Label))
                {
                    e.Graphics.DrawString(c.Text, c.Font, new SolidBrush(c.ForeColor), c.Location);
                }

            }

        }
        public static void GetPrintStrRefund2(object sender, PrintPageEventArgs e)
        {

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
            type.Text = printDocumentChild.print.name;
            type.Left = left;
            type.Font = new Font("微软雅黑", 10);
            type.ForeColor = Color.Gray;
            type.Top = top + type.Height + shopName.Top - 3;

            Label u = new Label();
            u.Text = "-------------------退菜----------------------";
            u.Left = left;
            u.Font = new Font("微软雅黑", 10);
            u.ForeColor = Color.Gray;
            u.Top = top + u.Height + type.Top - 3;
            Label dNumber = new Label();
            Label odinal = new Label();
            odinal.Text = "订单序号: " + printDocumentChild.orderInfo.ordinal;
            odinal.Left = left;
            odinal.Font = new Font("微软雅黑", 10);
            odinal.ForeColor = Color.Gray;

            if (printDocumentChild.orderInfo.deskName != null)
            {
                dNumber.Text = "桌台: " + printDocumentChild.orderInfo.deskName;
                dNumber.Left = left;
                dNumber.Font = new Font("微软雅黑", 10);
                dNumber.ForeColor = Color.Gray;
                dNumber.Top = top + dNumber.Height + u.Top - 3;
                odinal.Top = top + odinal.Height + dNumber.Top - 3;
                p.Controls.Add(dNumber);
            }
            else
            {
                odinal.Top = top + odinal.Height + u.Top - 3;
            }

            Label peocount = new Label();
            peocount.Text = "人数: " + printDocumentChild.orderInfo.peocount;
            peocount.Left = left + 150;
            peocount.Font = new Font("微软雅黑", 10);
            peocount.ForeColor = Color.Gray;
            peocount.Top = top + dNumber.Height + u.Top - 3;
            p.Controls.Add(peocount);

            Label printDate = new Label();
            printDate.Text = "打印日期: " + DateTime.Now.ToShortDateString();
            printDate.Left = left;
            printDate.Font = new Font("微软雅黑", 10);
            printDate.ForeColor = Color.Gray;
            printDate.Top = top + printDate.Height + odinal.Top - 3;

            Label orderNo = new Label();
            orderNo.Text = "单号: " + printDocumentChild.orderInfo.orderNO;
            orderNo.Left = left;
            orderNo.Font = new Font("微软雅黑", 10);
            orderNo.ForeColor = Color.Gray;
            orderNo.Top = top + orderNo.Height + printDate.Top - 3;


            System.DateTime orderTIME = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
            DateTime dt = orderTIME.AddMilliseconds(printDocumentChild.orderInfo.createTime);
            Label orderDate = new Label();
            orderDate.Text = "下单时间: " + dt.ToString("yyyy/MM/dd HH:mm:ss");
            orderDate.Left = left;
            orderDate.Font = new Font("微软雅黑", 10);
            orderDate.ForeColor = Color.Gray;
            orderDate.Top = top + orderDate.Height + orderNo.Top - 3;

            Label u2 = new Label();
            u2.Text = "--------------------------------------------";
            u2.Left = left;
            u2.Font = new Font("微软雅黑", 10);
            u2.ForeColor = Color.Gray;
            u2.Top = top + u2.Height + orderDate.Top - 3;

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
            cpPrice.Text = "单价";
            cpPrice.Left = left + 230;
            cpPrice.Font = new Font("微软雅黑", 10);
            cpPrice.ForeColor = Color.Gray;
            cpPrice.Top = top + cpName.Height + u2.Top;
            p.Controls.Add(cpName);
            p.Controls.Add(cpCount);
            p.Controls.Add(cpPrice);
            int t = 0;
            int l = cpPrice.Top;
            // for (int i = 0; i < printDocumentChild.print.cps.Count; i++)

            // {
            Label name = new Label();
            name.Left = left;
            name.ForeColor = Color.Gray;

            Label count = new Label();
            count.Left = left + 150;

            count.ForeColor = Color.Gray;

            Label price = new Label();
            price.Left = left + 235;

            price.ForeColor = Color.Gray;

            name.Top = top + name.Height + l;


            count.Top = top + count.Height + l;


            price.Top = top + price.Height + l;

            t = price.Top;

            if (printDocumentChild.print.cp2.attrs != null)
            {
                string attrs = "";
                for (int j = 0; j < printDocumentChild.print.cp2.attrs.Count; j++)
                {
                    if (printDocumentChild.print.cp2.attrs.Count - 1 == j)
                    {
                        attrs += printDocumentChild.print.cp2.attrs[j].text;
                    }
                    else
                    {
                        attrs += printDocumentChild.print.cp2.attrs[j].text + ",";
                    }

                }

                name.Text = printDocumentChild.print.cp2.name + "(" + attrs + ")";

                count.Text = printDocumentChild.print.cp2.count.ToString();
                price.Text = printDocumentChild.print.cp2.price.ToString();

            }
            else
            {
                name.Text = printDocumentChild.print.cp2.name;

                count.Text = printDocumentChild.print.cp2.count.ToString();
                price.Text = printDocumentChild.print.cp2.price.ToString();

            }
            if (printDocumentChild.print.cp2.flag == -1)
            {
                name.Text += "(退)";
                name.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                price.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                count.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
            }
            else
            {
                price.Font = new Font("微软雅黑", 10);
                name.Font = new Font("微软雅黑", 10);
                count.Font = new Font("微软雅黑", 10);
            }
            p.Controls.Add(name);
            p.Controls.Add(count);
            p.Controls.Add(price);

            //  }

            Label u3 = new Label();
            u3.Text = "--------------------------------------------";
            u3.Left = left;
            u3.Font = new Font("微软雅黑", 10);
            u3.ForeColor = Color.Gray;
            u3.Top = top + u3.Height + t;
            p.Controls.Add(u3);


            Label primeMoney = new Label();
            primeMoney.Text = "单价: ￥" + printDocumentChild.print.cp2.price;
            primeMoney.Left = left;
            primeMoney.Font = new Font("微软雅黑", 10);
            primeMoney.ForeColor = Color.Gray;
            primeMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(primeMoney);

            Label memberMoney = new Label();
            memberMoney.Text = "会员价: ￥" + printDocumentChild.print.cp2.memberPrice;
            memberMoney.Left = left + 150;
            memberMoney.Font = new Font("微软雅黑", 10);
            memberMoney.ForeColor = Color.Gray;
            memberMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(memberMoney);

            Label remark = new Label();
            remark.Text = "订单备注: " + printDocumentChild.orderInfo.remark;
            remark.Left = left;
            remark.Font = new Font("微软雅黑", 10);
            remark.ForeColor = Color.Gray;
            remark.Top = top + remark.Height + memberMoney.Top;
            p.Controls.Add(remark);

            Label cpremark = new Label();
            cpremark.AutoSize = false;

            cpremark.Text = "菜品备注: " + printDocumentChild.print.cp2.remark;
            int jd = cpremark.Text.Length / 20;
            if (cpremark.Text.Length != 20 && cpremark.Text.Length > 20)
            {
                for (int m = 1; m <= jd; m++)
                {
                    if (m != 1)
                    {
                        cpremark.Text = cpremark.Text.Insert(m * 20 + 1, "\r\n");
                    }
                    else
                    {
                        cpremark.Text = cpremark.Text.Insert(m * 20, "\r\n");
                    }

                }
            }
            //if (cpremark.Text.Length>21)
            // {
            //     cpremark.Text= cpremark.Text.Insert(21,"\r\n");              
            // }
            cpremark.Left = left;
            cpremark.Font = new Font("微软雅黑", 10);
            cpremark.ForeColor = Color.Gray;
            cpremark.Top = top + cpremark.Height + remark.Top;
            p.Controls.Add(cpremark);

            p.Controls.Add(shopName);
            p.Controls.Add(type);
            p.Controls.Add(u);
            p.Controls.Add(dNumber);
            p.Controls.Add(odinal);
            p.Controls.Add(printDate);
            p.Controls.Add(orderNo);
            p.Controls.Add(orderDate);
            p.Controls.Add(u2);


            foreach (Control c in p.Controls)
            {
                if (c.GetType() == typeof(Label))
                {
                    e.Graphics.DrawString(c.Text, c.Font, new SolidBrush(c.ForeColor), c.Location);
                }

            }

        }


        public static void GetPrintStrTest(object sender, PrintPageEventArgs e)

        {

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
            type.Text = printDocumentChild.print.name;
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
            Label odinal = new Label();
            odinal.Text = "订单序号: " + printDocumentChild.orderInfo.ordinal;
            odinal.Left = left;
            odinal.Font = new Font("微软雅黑", 10);
            odinal.ForeColor = Color.Gray;

            if (printDocumentChild.orderInfo.deskName != null)
            {
                dNumber.Text = "桌台: " + printDocumentChild.orderInfo.deskName;
                dNumber.Left = left;
                dNumber.Font = new Font("微软雅黑", 10);
                dNumber.ForeColor = Color.Gray;
                dNumber.Top = top + dNumber.Height + u.Top - 3;
                odinal.Top = top + odinal.Height + dNumber.Top - 3;
                p.Controls.Add(dNumber);
            }
            else
            {
                odinal.Top = top + odinal.Height + u.Top - 3;
            }
            Label peocount = new Label();
            peocount.Text = "人数: " + printDocumentChild.orderInfo.peocount;
            peocount.Left = left + 150;
            peocount.Font = new Font("微软雅黑", 10);
            peocount.ForeColor = Color.Gray;
            peocount.Top = top + dNumber.Height + u.Top - 3;
            p.Controls.Add(peocount);


            Label printDate = new Label();
            printDate.Text = "打印日期: " + DateTime.Now.ToShortDateString();
            printDate.Left = left;
            printDate.Font = new Font("微软雅黑", 10);
            printDate.ForeColor = Color.Gray;
            printDate.Top = top + printDate.Height + odinal.Top - 3;

            Label orderNo = new Label();
            orderNo.Text = "单号: " + printDocumentChild.orderInfo.orderNO;
            orderNo.Left = left;
            orderNo.Font = new Font("微软雅黑", 10);
            orderNo.ForeColor = Color.Gray;
            orderNo.Top = top + orderNo.Height + printDate.Top - 3;


            System.DateTime orderTIME = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
            DateTime dt = orderTIME.AddMilliseconds(printDocumentChild.orderInfo.createTime);
            Label orderDate = new Label();
            orderDate.Text = "下单时间: " + dt.ToString("yyyy/MM/dd HH:mm:ss"); ;
            orderDate.Left = left;
            orderDate.Font = new Font("微软雅黑", 10);
            orderDate.ForeColor = Color.Gray;
            orderDate.Top = top + orderDate.Height + orderNo.Top - 3;

            Label u2 = new Label();
            u2.Text = "--------------------------------------------";
            u2.Left = left;
            u2.Font = new Font("微软雅黑", 10);
            u2.ForeColor = Color.Gray;
            u2.Top = top + u2.Height + orderDate.Top - 3;

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
            cpPrice.Text = "单价";
            cpPrice.Left = left + 230;
            cpPrice.Font = new Font("微软雅黑", 10);
            cpPrice.ForeColor = Color.Gray;
            cpPrice.Top = top + cpName.Height + u2.Top;
            p.Controls.Add(cpName);
            p.Controls.Add(cpCount);
            p.Controls.Add(cpPrice);
            int t = 0;
            int l = 0;
            int nameTop = 0;
            int countTop = 0;
            int priceTop = 0;
            int all = 0;
            for (int i = 0; i < printDocumentChild.print.cps.Count; i++)
            {
                
                if (i != 0 && printDocumentChild.print.cps[i - 1].subItems != null && printDocumentChild.print.cps[i].subItems == null)
                {
                    if (printDocumentChild.print.cps[i - 1].cpChild.Count > 2)
                    {
                        // l += 10;
                    }
                    else
                    {
                        // l += 20;
                    }

                }
                Label name = new Label();
                name.AutoSize = true;
                name.Left = left;
                name.ForeColor = Color.Gray;
                Label count = new Label();
                count.Left = left + 150;
                count.ForeColor = Color.Gray;
                Label price = new Label();
                price.Left = left + 235;
                price.ForeColor = Color.Gray;

                //  name.Top = top + name.Height + cpPrice.Top + l + i * 20;
                //  count.Top = top + count.Height + cpPrice.Top + l + i * 20;
                //  price.Top = top + price.Height + cpPrice.Top + l + i * 20;

                  name.Top = top + name.Height + cpPrice.Top + l + i * 20+nameTop;
                  count.Top = top + count.Height + cpPrice.Top + l + i * 20+countTop;
                  price.Top = top + price.Height + cpPrice.Top + l + i * 20+priceTop;

                if (printDocumentChild.print.cps[i].attrs != null)
                {
                    string attrs = "";
                    for (int j = 0; j < printDocumentChild.print.cps[i].attrs.Count; j++)
                    {
                        if (printDocumentChild.print.cps[i].attrs.Count - 1 == j)
                        {
                            attrs += printDocumentChild.print.cps[i].attrs[j].text;
                        }
                        else
                        {
                            attrs += printDocumentChild.print.cps[i].attrs[j].text + ",";
                        }

                    }

                    name.Text = printDocumentChild.print.cps[i].name + "(" + attrs + ")";
                    count.Text = printDocumentChild.print.cps[i].count.ToString();
                    price.Text = printDocumentChild.print.cps[i].price.ToString();

                }
                else
                {
                    name.Text = printDocumentChild.print.cps[i].name;
                    count.Text = printDocumentChild.print.cps[i].count.ToString();
                    price.Text = printDocumentChild.print.cps[i].price.ToString();

                }
               
               


                if (printDocumentChild.print.cps[i].subItems != null)
                {
                    for (int j = 0; j < printDocumentChild.print.cps[i].cpChild.Count; j++)
                    {
                        name.Text += "\r\n     ↳" + printDocumentChild.print.cps[i].cpChild[j].name;

                        if (printDocumentChild.print.cps[i].cpChild.Count <= 2)
                        {
                            l += 20;
                        }
                        else
                        {
                            l += 20;
                        }
                    }

                }


                if (printDocumentChild.print.cps[i].flag == -1)
                {
                    name.Text += "(退)";
                    name.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    price.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    count.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                }
                else
                {
                    price.Font = new Font("微软雅黑", 10);
                    name.Font = new Font("微软雅黑", 10);
                    count.Font = new Font("微软雅黑", 10);
                }

                if (printDocumentChild.print.cps[i].remark != null && printDocumentChild.print.cps[i].remark != "")
                {
                    string nn = "\r\n   (注)" + printDocumentChild.print.cps[i].remark;
                    int jd = nn.Length / 20;
                    if (nn.Length != 20 &&nn.Length > 20)
                    {
                        for (int m = 1; m <= jd; m++)
                        {
                            if (m != 1)
                            {
                                nn = nn.Insert(m * 20+5, "\r\n   ");
                            }
                            else
                            {
                                nn = nn.Insert(m * 23 , "\r\n   ");
                            }
                          
                            
                        }
                    }
                    else
                    {
                        jd = 0;
                    }

                    if (jd == 0)
                    {
                        nameTop += 20;
                        countTop += 20;
                        priceTop += 20;
                        all = 20;
                    }
                    else
                    if (jd == 1)
                    {
                        nameTop += 40;
                        countTop += 40;
                        priceTop += 40;
                        all = 40;
                    }
                    else
                    {
                        nameTop += jd * 20+20;
                        countTop += jd * 20+20;
                        priceTop += jd * 20+20;
                        all = jd * 20;
                    }
                    name.Text += nn;
                }
                else
                {
                    all = 20;
                }
                
                p.Controls.Add(name);
                p.Controls.Add(count);
                p.Controls.Add(price);



                if (printDocumentChild.print.cps.Count == 1)
                {
                    if (printDocumentChild.print.cps[i].remark != null && printDocumentChild.print.cps[i].remark != "")
                    {
                        t = l + price.Top + all;

                    }
                    else
                    {
                        t = l + price.Top;
                    }
                       
                }
                else
                {
                    t = price.Top + all;
                }


            }

            Label u3 = new Label();
            u3.Text = "--------------------------------------------";
            u3.Left = left;
            u3.Font = new Font("微软雅黑", 10);
            u3.ForeColor = Color.Gray;
            u3.Top = top + u3.Height + t;
            p.Controls.Add(u3);

            Label primeMoney = new Label();
            primeMoney.Text = "合计: ￥" + printDocumentChild.orderInfo.primeMoney;
            primeMoney.Left = left;
            primeMoney.Font = new Font("微软雅黑", 10);
            primeMoney.ForeColor = Color.Gray;
            primeMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(primeMoney);

            Label memberMoney = new Label();
            memberMoney.Text = "会员价: ￥" + printDocumentChild.orderInfo.memberMoney;
            memberMoney.Left = left + 150;
            memberMoney.Font = new Font("微软雅黑", 10);
            memberMoney.ForeColor = Color.Gray;
            memberMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(memberMoney);

            Label remark = new Label();
            remark.Text = "备注: " + printDocumentChild.orderInfo.remark;
            remark.Left = left;
            remark.Font = new Font("微软雅黑", 10);
            remark.ForeColor = Color.Gray;
            remark.Top = top + remark.Height + primeMoney.Top;
            p.Controls.Add(remark);

           // Label cpremark = new Label();
           // cpremark.Text = "菜品备注: ";
           // cpremark.AutoSize = false;
           // for (int i = 0; i < printDocumentChild.print.cps.Count; i++)
           // {
          //      if (printDocumentChild.print.cps[i].remark!=null && printDocumentChild.print.cps[i].remark!="")
           //     {
           //         cpremark.Text += printDocumentChild.print.cps[i].name+":"+printDocumentChild.print.cps[i].remark+" ";

           //     }
           // }

          //  int jd=cpremark.Text.Length / 22;
          //  for (int m=1;m<=jd;m++)
          //  {
          //      cpremark.Text=cpremark.Text.Insert(m*22,"\r\n");
          //  }

         //   cpremark.Left = left;
         //   cpremark.Font = new Font("微软雅黑", 10);
         //   cpremark.ForeColor = Color.Gray;
         //   cpremark.Top = top + cpremark.Height + remark.Top;
         //   p.Controls.Add(cpremark);
            if (printDocumentChild.orderInfo.payStatus == 0)
            {

            }
            else if (printDocumentChild.orderInfo.payStatus == 1|| printDocumentChild.orderInfo.payStatus == 9)
            {
                Label u5 = new Label();
                u5.Text = "--------------------------------------------";
                u5.Left = left;
                u5.Font = new Font("微软雅黑", 10);
                u5.ForeColor = Color.Gray;
                //u5.Top = top + u5.Height + remark.Top;
                u5.Top = top + u5.Height + remark.Top;
                p.Controls.Add(u5);

                string payType = "";
                if (printDocumentChild.orderInfo.payType == 1)
                {
                    payType = "微信";
                }
                else if (printDocumentChild.orderInfo.payType == 2)
                {
                    payType = "支付宝";
                }
                else if (printDocumentChild.orderInfo.payType == 3)
                {
                    payType = "现金";

                }
                else if (printDocumentChild.orderInfo.payType == 4)
                {
                    payType = "刷卡";
                }
                else if (printDocumentChild.orderInfo.payType == 5)
                {
                    payType = "其他";
                }
                else if (printDocumentChild.orderInfo.payType == 10)
                {
                    payType = "会员卡";
                }
                else if (printDocumentChild.orderInfo.payType == 12)
                {
                    payType = "美团";
                }
                else if (printDocumentChild.orderInfo.payType == 13)
                {
                    payType = "饿了么";
                }


                Label payTypes = new Label();
                payTypes.Text = "支付方式: " + payType;
                payTypes.Left = left;
                payTypes.Font = new Font("微软雅黑", 10);
                payTypes.ForeColor = Color.Gray;
                payTypes.Top = top + payTypes.Height + u5.Top;
                p.Controls.Add(payTypes);

                Label paidMoney = new Label();
                if (printDocumentChild.orderInfo.payType == 3)
                {
                    if (printDocumentChild.orderInfo.cashMoney == 0)
                    {
                        paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.paidMoney; 
                    }
                    else
                    {
                        paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.cashMoney;
                    }
                  
                    Label y = new Label();
                    y.Text = "找零: ￥" + printDocumentChild.orderInfo.balanceMoney;
                    y.Left = left + 150;
                    y.Font = new Font("微软雅黑", 10);
                    y.ForeColor = Color.Gray;
                    y.Top = top + payTypes.Height + u5.Top;
                    p.Controls.Add(y);

                    Label yh = new Label();
                    yh.Text = "优惠: ￥" + printDocumentChild.orderInfo.yh;
                    yh.Left = left + 150;
                    yh.Font = new Font("微软雅黑", 10);
                    yh.ForeColor = Color.Gray;                  
                    yh.Top = top + paidMoney.Height + payTypes.Top;
                    p.Controls.Add(yh);

                }
                else
                {
                    paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.paidMoney;
                    Label y = new Label();
                    y.Text = "优惠: ￥" + printDocumentChild.orderInfo.yh;
                    y.Left = left + 150;
                    y.Font = new Font("微软雅黑", 10);
                    y.ForeColor = Color.Gray;
                    y.Top = top + paidMoney.Height + payTypes.Top;
                    p.Controls.Add(y);
                }

                paidMoney.Left = left;
                paidMoney.Font = new Font("微软雅黑", 10);
                paidMoney.ForeColor = Color.Gray;
                paidMoney.Top = top + paidMoney.Height + payTypes.Top;
                p.Controls.Add(paidMoney);

                Label payNO = new Label();
                payNO.Text = "支付号: " + printDocumentChild.orderInfo.payNO;
                payNO.Left = left;
                payNO.Font = new Font("微软雅黑", 10);
                payNO.ForeColor = Color.Gray;
                payNO.Top = top + payNO.Height + paidMoney.Top;
                p.Controls.Add(payNO);

                System.DateTime payTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                DateTime dt1 = payTime.AddMilliseconds(printDocumentChild.orderInfo.payTime);

                Label payTimes = new Label();
                payTimes.Text = "支付时间: " + dt1.ToString("yyyy/MM/dd HH:mm:ss");
                payTimes.Left = left;
                payTimes.Font = new Font("微软雅黑", 10);
                payTimes.ForeColor = Color.Gray;
                payTimes.Top = top + payTimes.Height + payNO.Top;
                p.Controls.Add(payTimes);

                Label u4 = new Label();
                u4.Text = "--------------------------------------------";
                u4.Left = left;
                u4.Font = new Font("微软雅黑", 10);
                u4.ForeColor = Color.Gray;
                u4.Top = top + u4.Height + payTimes.Top;
                p.Controls.Add(u4);

                Label address = new Label();
                address.Text = "地址: " + Login.userInfo.address;
                address.Left = left;
                address.Font = new Font("微软雅黑", 10);
                address.ForeColor = Color.Gray;
                address.Top = top + address.Height + u4.Top;
                p.Controls.Add(address);

                Label cusPhone = new Label();
                cusPhone.Text = "电话: " + Login.userInfo.cusPhone;
                cusPhone.Left = left;
                cusPhone.Font = new Font("微软雅黑", 10);
                cusPhone.ForeColor = Color.Gray;
                cusPhone.Top = top + cusPhone.Height + address.Top;
                p.Controls.Add(cusPhone);

                Label hello = new Label();
                hello.Text = "谢谢惠顾欢迎您下次光临";
                hello.Left = left + 70;
                hello.Font = new Font("微软雅黑", 10);
                hello.ForeColor = Color.Gray;
                hello.Top = top + hello.Height + cusPhone.Top;
                p.Controls.Add(hello);

            }
            else if (printDocumentChild.orderInfo.payStatus == 2)
            {
                Label u5 = new Label();
                u5.Text = "--------------------------------------------";
                u5.Left = left;
                u5.Font = new Font("微软雅黑", 10);
                u5.ForeColor = Color.Gray;
                //  u5.Top = top + u5.Height + remark.Top;
                u5.Top = top + u5.Height + remark.Top;
                p.Controls.Add(u5);

                string payType = "挂账";

                Label payTypes = new Label();
                payTypes.Text = "支付方式: " + payType;
                payTypes.Left = left;
                payTypes.Font = new Font("微软雅黑", 10);
                payTypes.ForeColor = Color.Gray;
                payTypes.Top = top + payTypes.Height + u5.Top;
                p.Controls.Add(payTypes);

                System.DateTime payTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                DateTime dt1 = payTime.AddMilliseconds(printDocumentChild.orderInfo.payTime);

                Label payTimes = new Label();
                payTimes.Text = "挂账时间: " + dt1.ToString("yyyy/MM/dd HH:mm:ss");
                payTimes.Left = left;
                payTimes.Font = new Font("微软雅黑", 10);
                payTimes.ForeColor = Color.Gray;
                payTimes.Top = top + payTimes.Height + payTypes.Top;
                p.Controls.Add(payTimes);

                Label u4 = new Label();
                u4.Text = "--------------------------------------------";
                u4.Left = left;
                u4.Font = new Font("微软雅黑", 10);
                u4.ForeColor = Color.Gray;
                u4.Top = top + u4.Height + payTimes.Top;
                p.Controls.Add(u4);

                Label address = new Label();
                address.Text = "地址: " + Login.userInfo.address;
                address.Left = left;
                address.Font = new Font("微软雅黑", 10);
                address.ForeColor = Color.Gray;
                address.Top = top + address.Height + u4.Top;
                p.Controls.Add(address);

                Label cusPhone = new Label();
                cusPhone.Text = "电话: " + Login.userInfo.cusPhone;
                cusPhone.Left = left;
                cusPhone.Font = new Font("微软雅黑", 10);
                cusPhone.ForeColor = Color.Gray;
                cusPhone.Top = top + cusPhone.Height + address.Top;
                p.Controls.Add(cusPhone);

                Label hello = new Label();
                hello.Text = "谢谢惠顾欢迎您下次光临";
                hello.Left = left + 70;
                hello.Font = new Font("微软雅黑", 10);
                hello.ForeColor = Color.Gray;
                hello.Top = top + hello.Height + cusPhone.Top;
                p.Controls.Add(hello);

            }


            p.Controls.Add(shopName);
            p.Controls.Add(type);
            p.Controls.Add(u);
            p.Controls.Add(dNumber);
            p.Controls.Add(odinal);
            p.Controls.Add(printDate);
            p.Controls.Add(orderNo);
            p.Controls.Add(orderDate);
            p.Controls.Add(u2);


            foreach (Control c in p.Controls)
            {
                if (c.GetType() == typeof(Label))
                {
                    e.Graphics.DrawString(c.Text, c.Font, new SolidBrush(c.ForeColor), c.Location);
                }

            }

        }
        public static void GetPrintStrRefund(object sender, PrintPageEventArgs e)

        {

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
            type.Text = printDocumentChild.print.name;
            type.Left = left;
            type.Font = new Font("微软雅黑", 10);
            type.ForeColor = Color.Gray;
            type.Top = top + type.Height + shopName.Top - 3;

            Label u = new Label();
            u.Text = "-------------------退菜-----------------------";
            u.Left = left;
            u.Font = new Font("微软雅黑", 10);
            u.ForeColor = Color.Gray;
            u.Top = top + u.Height + type.Top - 3;

            Label dNumber = new Label();
            Label odinal = new Label();
            odinal.Text = "订单序号: " + printDocumentChild.orderInfo.ordinal;
            odinal.Left = left;
            odinal.Font = new Font("微软雅黑", 10);
            odinal.ForeColor = Color.Gray;

            if (printDocumentChild.orderInfo.deskName != null)
            {
                dNumber.Text = "桌台: " + printDocumentChild.orderInfo.deskName;
                dNumber.Left = left;
                dNumber.Font = new Font("微软雅黑", 10);
                dNumber.ForeColor = Color.Gray;
                dNumber.Top = top + dNumber.Height + u.Top - 3;
                odinal.Top = top + odinal.Height + dNumber.Top - 3;
                p.Controls.Add(dNumber);
            }
            else
            {
                odinal.Top = top + odinal.Height + u.Top - 3;
            }
            Label peocount = new Label();
            peocount.Text = "人数: " + printDocumentChild.orderInfo.peocount;
            peocount.Left = left + 150;
            peocount.Font = new Font("微软雅黑", 10);
            peocount.ForeColor = Color.Gray;
            peocount.Top = top + dNumber.Height + u.Top - 3;
            p.Controls.Add(peocount);


            Label printDate = new Label();
            printDate.Text = "打印日期: " + DateTime.Now.ToShortDateString();
            printDate.Left = left;
            printDate.Font = new Font("微软雅黑", 10);
            printDate.ForeColor = Color.Gray;
            printDate.Top = top + printDate.Height + odinal.Top - 3;

            Label orderNo = new Label();
            orderNo.Text = "单号: " + printDocumentChild.orderInfo.orderNO;
            orderNo.Left = left;
            orderNo.Font = new Font("微软雅黑", 10);
            orderNo.ForeColor = Color.Gray;
            orderNo.Top = top + orderNo.Height + printDate.Top - 3;


            System.DateTime orderTIME = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
            DateTime dt = orderTIME.AddMilliseconds(printDocumentChild.orderInfo.createTime);
            Label orderDate = new Label();
            orderDate.Text = "下单时间: " + dt.ToString("yyyy/MM/dd HH:mm:ss"); ;
            orderDate.Left = left;
            orderDate.Font = new Font("微软雅黑", 10);
            orderDate.ForeColor = Color.Gray;
            orderDate.Top = top + orderDate.Height + orderNo.Top - 3;

            Label u2 = new Label();
            u2.Text = "--------------------------------------------";
            u2.Left = left;
            u2.Font = new Font("微软雅黑", 10);
            u2.ForeColor = Color.Gray;
            u2.Top = top + u2.Height + orderDate.Top - 3;

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
            cpPrice.Text = "单价";
            cpPrice.Left = left + 230;
            cpPrice.Font = new Font("微软雅黑", 10);
            cpPrice.ForeColor = Color.Gray;
            cpPrice.Top = top + cpName.Height + u2.Top;
            p.Controls.Add(cpName);
            p.Controls.Add(cpCount);
            p.Controls.Add(cpPrice);
            int t = 0;
            int l = 0;
            int nameTop = 0;
            int countTop = 0;
            int priceTop = 0;
            int all = 0;
            for (int i = 0; i < printDocumentChild.print.cpsRefund.Count; i++)
            {

                if (i != 0 && printDocumentChild.print.cpsRefund[i - 1].subItems != null && printDocumentChild.print.cpsRefund[i].subItems == null)
                {
                    if (printDocumentChild.print.cpsRefund[i - 1].cpChild.Count > 2)
                    {
                        // l += 10;
                    }
                    else
                    {
                        // l += 20;
                    }

                }
                Label name = new Label();
                name.AutoSize = true;
                name.Left = left;
                name.ForeColor = Color.Gray;
                Label count = new Label();
                count.Left = left + 150;
                count.ForeColor = Color.Gray;
                Label price = new Label();
                price.Left = left + 235;
                price.ForeColor = Color.Gray;

                //  name.Top = top + name.Height + cpPrice.Top + l + i * 20;
                //  count.Top = top + count.Height + cpPrice.Top + l + i * 20;
                //  price.Top = top + price.Height + cpPrice.Top + l + i * 20;

                name.Top = top + name.Height + cpPrice.Top + l + i * 20 + nameTop;
                count.Top = top + count.Height + cpPrice.Top + l + i * 20 + countTop;
                price.Top = top + price.Height + cpPrice.Top + l + i * 20 + priceTop;

                if (printDocumentChild.print.cpsRefund[i].attrs != null)
                {
                    string attrs = "";
                    for (int j = 0; j < printDocumentChild.print.cpsRefund[i].attrs.Count; j++)
                    {
                        if (printDocumentChild.print.cpsRefund[i].attrs.Count - 1 == j)
                        {
                            attrs += printDocumentChild.print.cpsRefund[i].attrs[j].text;
                        }
                        else
                        {
                            attrs += printDocumentChild.print.cpsRefund[i].attrs[j].text + ",";
                        }

                    }

                    name.Text = printDocumentChild.print.cpsRefund[i].name + "(" + attrs + ")";
                    count.Text = printDocumentChild.print.cpsRefund[i].count.ToString();
                    price.Text = printDocumentChild.print.cpsRefund[i].price.ToString();

                }
                else
                {
                    name.Text = printDocumentChild.print.cpsRefund[i].name;
                    count.Text = printDocumentChild.print.cpsRefund[i].count.ToString();
                    price.Text = printDocumentChild.print.cpsRefund[i].price.ToString();

                }




                if (printDocumentChild.print.cpsRefund[i].subItems != null)
                {
                    for (int j = 0; j < printDocumentChild.print.cpsRefund[i].cpChild.Count; j++)
                    {
                        name.Text += "\r\n     ↳" + printDocumentChild.print.cpsRefund[i].cpChild[j].name;

                        if (printDocumentChild.print.cpsRefund[i].cpChild.Count <= 2)
                        {
                            l += 20;
                        }
                        else
                        {
                            l += 20;
                        }
                    }

                }


                if (printDocumentChild.print.cpsRefund[i].flag == -1)
                {
                    name.Text += "(退)";
                    name.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    price.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    count.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                }
                else
                {
                    price.Font = new Font("微软雅黑", 10);
                    name.Font = new Font("微软雅黑", 10);
                    count.Font = new Font("微软雅黑", 10);
                }

                if (printDocumentChild.print.cpsRefund[i].remark != null && printDocumentChild.print.cpsRefund[i].remark != "")
                {
                    string nn = "\r\n   (注)" + printDocumentChild.print.cpsRefund[i].remark;
                    int jd = nn.Length / 20;
                    if (nn.Length != 20 && nn.Length > 20)
                    {
                        for (int m = 1; m <= jd; m++)
                        {
                            if (m != 1)
                            {
                                nn = nn.Insert(m * 20 + 5, "\r\n   ");
                            }
                            else
                            {
                                nn = nn.Insert(m * 23, "\r\n   ");
                            }


                        }
                    }
                    else
                    {
                        jd = 0;
                    }

                    if (jd == 0)
                    {
                        nameTop += 20;
                        countTop += 20;
                        priceTop += 20;
                        all = 20;
                    }
                    else
                    if (jd == 1)
                    {
                        nameTop += 40;
                        countTop += 40;
                        priceTop += 40;
                        all = 40;
                    }
                    else
                    {
                        nameTop += jd * 20 + 20;
                        countTop += jd * 20 + 20;
                        priceTop += jd * 20 + 20;
                        all = jd * 20;
                    }
                    name.Text += nn;
                }
                else
                {
                    all = 20;
                }

                p.Controls.Add(name);
                p.Controls.Add(count);
                p.Controls.Add(price);



                if (printDocumentChild.print.cpsRefund.Count == 1)
                {
                    if (printDocumentChild.print.cpsRefund[i].remark != null && printDocumentChild.print.cpsRefund[i].remark != "")
                    {
                        t = l + price.Top + all;

                    }
                    else
                    {
                        t = l + price.Top;
                    }

                }
                else
                {
                    t = price.Top + all;
                }


            }

            Label u3 = new Label();
            u3.Text = "--------------------------------------------";
            u3.Left = left;
            u3.Font = new Font("微软雅黑", 10);
            u3.ForeColor = Color.Gray;
            u3.Top = top + u3.Height + t;
            p.Controls.Add(u3);

            Label primeMoney = new Label();
            primeMoney.Text = "合计: ￥" + printDocumentChild.orderInfo.primeMoney;
            primeMoney.Left = left;
            primeMoney.Font = new Font("微软雅黑", 10);
            primeMoney.ForeColor = Color.Gray;
            primeMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(primeMoney);

            Label memberMoney = new Label();
            memberMoney.Text = "会员价: ￥" + printDocumentChild.orderInfo.memberMoney;
            memberMoney.Left = left + 150;
            memberMoney.Font = new Font("微软雅黑", 10);
            memberMoney.ForeColor = Color.Gray;
            memberMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(memberMoney);

            Label remark = new Label();
            remark.Text = "备注: " + printDocumentChild.orderInfo.remark;
            remark.Left = left;
            remark.Font = new Font("微软雅黑", 10);
            remark.ForeColor = Color.Gray;
            remark.Top = top + remark.Height + primeMoney.Top;
            p.Controls.Add(remark);

            // Label cpremark = new Label();
            // cpremark.Text = "菜品备注: ";
            // cpremark.AutoSize = false;
            // for (int i = 0; i < printDocumentChild.print.cps.Count; i++)
            // {
            //      if (printDocumentChild.print.cps[i].remark!=null && printDocumentChild.print.cps[i].remark!="")
            //     {
            //         cpremark.Text += printDocumentChild.print.cps[i].name+":"+printDocumentChild.print.cps[i].remark+" ";

            //     }
            // }

            //  int jd=cpremark.Text.Length / 22;
            //  for (int m=1;m<=jd;m++)
            //  {
            //      cpremark.Text=cpremark.Text.Insert(m*22,"\r\n");
            //  }

            //   cpremark.Left = left;
            //   cpremark.Font = new Font("微软雅黑", 10);
            //   cpremark.ForeColor = Color.Gray;
            //   cpremark.Top = top + cpremark.Height + remark.Top;
            //   p.Controls.Add(cpremark);
            if (printDocumentChild.orderInfo.payStatus == 0)
            {

            }
            else if (printDocumentChild.orderInfo.payStatus == 1 || printDocumentChild.orderInfo.payStatus == 9)
            {
                Label u5 = new Label();
                u5.Text = "--------------------------------------------";
                u5.Left = left;
                u5.Font = new Font("微软雅黑", 10);
                u5.ForeColor = Color.Gray;
                //u5.Top = top + u5.Height + remark.Top;
                u5.Top = top + u5.Height + remark.Top;
                p.Controls.Add(u5);

                string payType = "";
                if (printDocumentChild.orderInfo.payType == 1)
                {
                    payType = "微信";
                }
                else if (printDocumentChild.orderInfo.payType == 2)
                {
                    payType = "支付宝";
                }
                else if (printDocumentChild.orderInfo.payType == 3)
                {
                    payType = "现金";

                }
                else if (printDocumentChild.orderInfo.payType == 4)
                {
                    payType = "刷卡";
                }
                else if (printDocumentChild.orderInfo.payType == 5)
                {
                    payType = "其他";
                }
                else if (printDocumentChild.orderInfo.payType == 10)
                {
                    payType = "会员卡";
                }


                Label payTypes = new Label();
                payTypes.Text = "支付方式: " + payType;
                payTypes.Left = left;
                payTypes.Font = new Font("微软雅黑", 10);
                payTypes.ForeColor = Color.Gray;
                payTypes.Top = top + payTypes.Height + u5.Top;
                p.Controls.Add(payTypes);

                Label paidMoney = new Label();
                if (printDocumentChild.orderInfo.payType == 3)
                {
                    if (printDocumentChild.orderInfo.cashMoney == 0)
                    {
                        paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.paidMoney;
                    }
                    else
                    {
                        paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.cashMoney;
                    }

                    Label y = new Label();
                    y.Text = "找零: ￥" + printDocumentChild.orderInfo.balanceMoney;
                    y.Left = left + 150;
                    y.Font = new Font("微软雅黑", 10);
                    y.ForeColor = Color.Gray;
                    y.Top = top + payTypes.Height + u5.Top;
                    p.Controls.Add(y);

                    Label yh = new Label();
                    yh.Text = "优惠: ￥" + printDocumentChild.orderInfo.yh;
                    yh.Left = left + 150;
                    yh.Font = new Font("微软雅黑", 10);
                    yh.ForeColor = Color.Gray;
                    yh.Top = top + paidMoney.Height + payTypes.Top;
                    p.Controls.Add(yh);

                }
                else
                {
                    paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.paidMoney;
                    Label y = new Label();
                    y.Text = "优惠: ￥" + printDocumentChild.orderInfo.yh;
                    y.Left = left + 150;
                    y.Font = new Font("微软雅黑", 10);
                    y.ForeColor = Color.Gray;
                    y.Top = top + paidMoney.Height + payTypes.Top;
                    p.Controls.Add(y);
                }

                paidMoney.Left = left;
                paidMoney.Font = new Font("微软雅黑", 10);
                paidMoney.ForeColor = Color.Gray;
                paidMoney.Top = top + paidMoney.Height + payTypes.Top;
                p.Controls.Add(paidMoney);

                Label payNO = new Label();
                payNO.Text = "支付号: " + printDocumentChild.orderInfo.payNO;
                payNO.Left = left;
                payNO.Font = new Font("微软雅黑", 10);
                payNO.ForeColor = Color.Gray;
                payNO.Top = top + payNO.Height + paidMoney.Top;
                p.Controls.Add(payNO);

                System.DateTime payTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                DateTime dt1 = payTime.AddMilliseconds(printDocumentChild.orderInfo.payTime);

                Label payTimes = new Label();
                payTimes.Text = "支付时间: " + dt1.ToString("yyyy/MM/dd HH:mm:ss");
                payTimes.Left = left;
                payTimes.Font = new Font("微软雅黑", 10);
                payTimes.ForeColor = Color.Gray;
                payTimes.Top = top + payTimes.Height + payNO.Top;
                p.Controls.Add(payTimes);

                Label u4 = new Label();
                u4.Text = "--------------------------------------------";
                u4.Left = left;
                u4.Font = new Font("微软雅黑", 10);
                u4.ForeColor = Color.Gray;
                u4.Top = top + u4.Height + payTimes.Top;
                p.Controls.Add(u4);

                Label address = new Label();
                address.Text = "地址: " + Login.userInfo.address;
                address.Left = left;
                address.Font = new Font("微软雅黑", 10);
                address.ForeColor = Color.Gray;
                address.Top = top + address.Height + u4.Top;
                p.Controls.Add(address);

                Label cusPhone = new Label();
                cusPhone.Text = "电话: " + Login.userInfo.cusPhone;
                cusPhone.Left = left;
                cusPhone.Font = new Font("微软雅黑", 10);
                cusPhone.ForeColor = Color.Gray;
                cusPhone.Top = top + cusPhone.Height + address.Top;
                p.Controls.Add(cusPhone);

                Label hello = new Label();
                hello.Text = "谢谢惠顾欢迎您下次光临";
                hello.Left = left + 70;
                hello.Font = new Font("微软雅黑", 10);
                hello.ForeColor = Color.Gray;
                hello.Top = top + hello.Height + cusPhone.Top;
                p.Controls.Add(hello);

            }
            else if (printDocumentChild.orderInfo.payStatus == 2)
            {
                Label u5 = new Label();
                u5.Text = "--------------------------------------------";
                u5.Left = left;
                u5.Font = new Font("微软雅黑", 10);
                u5.ForeColor = Color.Gray;
                //  u5.Top = top + u5.Height + remark.Top;
                u5.Top = top + u5.Height + remark.Top;
                p.Controls.Add(u5);

                string payType = "挂账";

                Label payTypes = new Label();
                payTypes.Text = "支付方式: " + payType;
                payTypes.Left = left;
                payTypes.Font = new Font("微软雅黑", 10);
                payTypes.ForeColor = Color.Gray;
                payTypes.Top = top + payTypes.Height + u5.Top;
                p.Controls.Add(payTypes);

                System.DateTime payTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                DateTime dt1 = payTime.AddMilliseconds(printDocumentChild.orderInfo.payTime);

                Label payTimes = new Label();
                payTimes.Text = "挂账时间: " + dt1.ToString("yyyy/MM/dd HH:mm:ss");
                payTimes.Left = left;
                payTimes.Font = new Font("微软雅黑", 10);
                payTimes.ForeColor = Color.Gray;
                payTimes.Top = top + payTimes.Height + payTypes.Top;
                p.Controls.Add(payTimes);

                Label u4 = new Label();
                u4.Text = "--------------------------------------------";
                u4.Left = left;
                u4.Font = new Font("微软雅黑", 10);
                u4.ForeColor = Color.Gray;
                u4.Top = top + u4.Height + payTimes.Top;
                p.Controls.Add(u4);

                Label address = new Label();
                address.Text = "地址: " + Login.userInfo.address;
                address.Left = left;
                address.Font = new Font("微软雅黑", 10);
                address.ForeColor = Color.Gray;
                address.Top = top + address.Height + u4.Top;
                p.Controls.Add(address);

                Label cusPhone = new Label();
                cusPhone.Text = "电话: " + Login.userInfo.cusPhone;
                cusPhone.Left = left;
                cusPhone.Font = new Font("微软雅黑", 10);
                cusPhone.ForeColor = Color.Gray;
                cusPhone.Top = top + cusPhone.Height + address.Top;
                p.Controls.Add(cusPhone);

                Label hello = new Label();
                hello.Text = "谢谢惠顾欢迎您下次光临";
                hello.Left = left + 70;
                hello.Font = new Font("微软雅黑", 10);
                hello.ForeColor = Color.Gray;
                hello.Top = top + hello.Height + cusPhone.Top;
                p.Controls.Add(hello);

            }


            p.Controls.Add(shopName);
            p.Controls.Add(type);
            p.Controls.Add(u);
            p.Controls.Add(dNumber);
            p.Controls.Add(odinal);
            p.Controls.Add(printDate);
            p.Controls.Add(orderNo);
            p.Controls.Add(orderDate);
            p.Controls.Add(u2);


            foreach (Control c in p.Controls)
            {
                if (c.GetType() == typeof(Label))
                {
                    e.Graphics.DrawString(c.Text, c.Font, new SolidBrush(c.ForeColor), c.Location);
                }

            }

        }
        public static void GetPrintAutoPay(object sender, PrintPageEventArgs e)

        {

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
            type.Text = printDocumentChild.print.name;
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
            Label odinal = new Label();
            odinal.Text = "订单序号: " + printDocumentChild.orderInfo.ordinal;
            odinal.Left = left;
            odinal.Font = new Font("微软雅黑", 10);
            odinal.ForeColor = Color.Gray;

            if (printDocumentChild.orderInfo.deskName != null)
            {
                dNumber.Text = "桌台: " + printDocumentChild.orderInfo.deskName;
                dNumber.Left = left;
                dNumber.Font = new Font("微软雅黑", 10);
                dNumber.ForeColor = Color.Gray;
                dNumber.Top = top + dNumber.Height + u.Top - 3;
                odinal.Top = top + odinal.Height + dNumber.Top - 3;
                p.Controls.Add(dNumber);
            }
            else
            {
                odinal.Top = top + odinal.Height + u.Top - 3;
            }

            Label peocount = new Label();
            peocount.Text = "人数: " + printDocumentChild.orderInfo.peocount;
            peocount.Left = left + 150;
            peocount.Font = new Font("微软雅黑", 10);
            peocount.ForeColor = Color.Gray;
            peocount.Top = top + dNumber.Height + u.Top - 3;
            p.Controls.Add(peocount);

            Label printDate = new Label();
            printDate.Text = "打印日期: " + DateTime.Now.ToShortDateString();
            printDate.Left = left;
            printDate.Font = new Font("微软雅黑", 10);
            printDate.ForeColor = Color.Gray;
            printDate.Top = top + printDate.Height + odinal.Top - 3;

            Label orderNo = new Label();
            orderNo.Text = "单号: " + printDocumentChild.orderInfo.orderNO;
            orderNo.Left = left;
            orderNo.Font = new Font("微软雅黑", 10);
            orderNo.ForeColor = Color.Gray;
            orderNo.Top = top + orderNo.Height + printDate.Top - 3;


            System.DateTime orderTIME = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
            DateTime dt = orderTIME.AddMilliseconds(printDocumentChild.orderInfo.createTime);
            Label orderDate = new Label();
            orderDate.Text = "下单时间: " + dt.ToString("yyyy/MM/dd HH:mm:ss"); ;
            orderDate.Left = left;
            orderDate.Font = new Font("微软雅黑", 10);
            orderDate.ForeColor = Color.Gray;
            orderDate.Top = top + orderDate.Height + orderNo.Top - 3;

            Label u2 = new Label();
            u2.Text = "--------------------------------------------";
            u2.Left = left;
            u2.Font = new Font("微软雅黑", 10);
            u2.ForeColor = Color.Gray;
            u2.Top = top + u2.Height + orderDate.Top - 3;

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
            cpPrice.Text = "单价";
            cpPrice.Left = left + 230;
            cpPrice.Font = new Font("微软雅黑", 10);
            cpPrice.ForeColor = Color.Gray;
            cpPrice.Top = top + cpName.Height + u2.Top;
            p.Controls.Add(cpName);
            p.Controls.Add(cpCount);
            p.Controls.Add(cpPrice);
            int t = 0;
            int l = 0;
            for (int i = 0; i < printDocumentChild.print.cps.Count; i++)

            {
                if (i != 0 && printDocumentChild.print.cps[i - 1].subItems != null && printDocumentChild.print.cps[i].subItems == null)
                {
                    if (printDocumentChild.print.cps[i - 1].cpChild.Count > 2)
                    {
                        // l += 10;
                    }
                    else
                    {
                        // l += 20;
                    }

                }
                Label name = new Label();
                name.AutoSize = true;
                name.Left = left;
                name.ForeColor = Color.Gray;
                Label count = new Label();
                count.Left = left + 150;
                count.ForeColor = Color.Gray;
                Label price = new Label();
                price.Left = left + 235;
                price.ForeColor = Color.Gray;
                name.Top = top + name.Height + cpPrice.Top + l + i * 20;
                count.Top = top + count.Height + cpPrice.Top + l + i * 20;
                price.Top = top + price.Height + cpPrice.Top + l + i * 20;


                if (printDocumentChild.print.cps[i].attrs != null)
                {
                    string attrs = "";
                    for (int j = 0; j < printDocumentChild.print.cps[i].attrs.Count; j++)
                    {
                        if (printDocumentChild.print.cps[i].attrs.Count - 1 == j)
                        {
                            attrs += printDocumentChild.print.cps[i].attrs[j].text;
                        }
                        else
                        {
                            attrs += printDocumentChild.print.cps[i].attrs[j].text + ",";
                        }

                    }

                    name.Text = printDocumentChild.print.cps[i].name + "(" + attrs + ")";
                    count.Text = printDocumentChild.print.cps[i].count.ToString();
                    price.Text = printDocumentChild.print.cps[i].price.ToString();

                }
                else
                {
                    name.Text = printDocumentChild.print.cps[i].name;
                    count.Text = printDocumentChild.print.cps[i].count.ToString();
                    price.Text = printDocumentChild.print.cps[i].price.ToString();

                }

                if (printDocumentChild.print.cps[i].subItems != null)
                {
                    for (int j = 0; j < printDocumentChild.print.cps[i].cpChild.Count; j++)
                    {
                        name.Text += "\r\n     ↳" + printDocumentChild.print.cps[i].cpChild[j].name;

                        if (printDocumentChild.print.cps[i].cpChild.Count <= 2)
                        {
                            l += j * 20 + 10;
                        }
                        else
                        {
                            l += j * 20;
                        }
                    }

                }


                if (printDocumentChild.print.cps[i].flag == -1)
                {
                    name.Text += "(退)";
                    name.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    price.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    count.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                }
                else
                {
                    price.Font = new Font("微软雅黑", 10);
                    name.Font = new Font("微软雅黑", 10);
                    count.Font = new Font("微软雅黑", 10);
                }
                p.Controls.Add(name);
                p.Controls.Add(count);
                p.Controls.Add(price);


                t = price.Top;



            }

            Label u3 = new Label();
            u3.Text = "--------------------------------------------";
            u3.Left = left;
            u3.Font = new Font("微软雅黑", 10);
            u3.ForeColor = Color.Gray;
            u3.Top = top + u3.Height + t;
            p.Controls.Add(u3);

            Label remark = new Label();
            remark.Text = "备注: " + printDocumentChild.orderInfo.remark;
            remark.Left = left;
            remark.Font = new Font("微软雅黑", 10);
            remark.ForeColor = Color.Gray;
            remark.Top = top + remark.Height + u3.Top;
            p.Controls.Add(remark);

            Label primeMoney = new Label();
            primeMoney.Text = "合计: ￥" + printDocumentChild.orderInfo.primeMoney;
            primeMoney.Left = left;
            primeMoney.Font = new Font("微软雅黑", 10);
            primeMoney.ForeColor = Color.Gray;
            primeMoney.Top = top + primeMoney.Height + remark.Top;
            p.Controls.Add(primeMoney);

            Label memberMoney = new Label();
            memberMoney.Text = "会员价: ￥" + printDocumentChild.orderInfo.memberMoney;
            memberMoney.Left = left + 150;
            memberMoney.Font = new Font("微软雅黑", 10);
            memberMoney.ForeColor = Color.Gray;
            memberMoney.Top = top + primeMoney.Height + remark.Top;
            p.Controls.Add(memberMoney);


            if (printDocumentChild.orderInfo.payStatus == 0)
            {

            }
            else if (printDocumentChild.orderInfo.payStatus == 1)
            {
                Label u5 = new Label();
                u5.Text = "--------------------------------------------";
                u5.Left = left;
                u5.Font = new Font("微软雅黑", 10);
                u5.ForeColor = Color.Gray;
                u5.Top = top + u5.Height + memberMoney.Top;
                p.Controls.Add(u5);

                string payType = "";
                if (printDocumentChild.orderInfo.payType == 1)
                {
                    payType = "微信";
                }
                else if (printDocumentChild.orderInfo.payType == 2)
                {
                    payType = "支付宝";
                }
                else if (printDocumentChild.orderInfo.payType == 3)
                {
                    payType = "现金";

                }
                else if (printDocumentChild.orderInfo.payType == 4)
                {
                    payType = "刷卡";
                }
                else if (printDocumentChild.orderInfo.payType == 5)
                {
                    payType = "其他";
                }
                else if (printDocumentChild.orderInfo.payType == 10)
                {
                    payType = "会员卡";
                }


                Label payTypes = new Label();
                payTypes.Text = "支付方式: " + payType;
                payTypes.Left = left;
                payTypes.Font = new Font("微软雅黑", 10);
                payTypes.ForeColor = Color.Gray;
                payTypes.Top = top + payTypes.Height + u5.Top;
                p.Controls.Add(payTypes);

                Label paidMoney = new Label();
                if (printDocumentChild.orderInfo.payType == 3)
                {
                    if (printDocumentChild.orderInfo.cashMoney == 0)
                    {
                        paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.paidMoney;
                    }
                    else
                    {
                        paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.cashMoney;
                    }

                    Label y = new Label();
                    y.Text = "找零: ￥" + printDocumentChild.orderInfo.balanceMoney;
                    y.Left = left + 150;
                    y.Font = new Font("微软雅黑", 10);
                    y.ForeColor = Color.Gray;
                    y.Top = top + paidMoney.Height + payTypes.Top;
                    p.Controls.Add(y);
                }
                else
                {
                    paidMoney.Text = "支付金额: ￥" + printDocumentChild.orderInfo.paidMoney;
                }

                paidMoney.Left = left;
                paidMoney.Font = new Font("微软雅黑", 10);
                paidMoney.ForeColor = Color.Gray;
                paidMoney.Top = top + paidMoney.Height + payTypes.Top;
                p.Controls.Add(paidMoney);

                Label payNO = new Label();
                payNO.Text = "支付号: " + printDocumentChild.orderInfo.payNO;
                payNO.Left = left;
                payNO.Font = new Font("微软雅黑", 10);
                payNO.ForeColor = Color.Gray;
                payNO.Top = top + payNO.Height + paidMoney.Top;
                p.Controls.Add(payNO);

                System.DateTime payTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                DateTime dt1 = payTime.AddMilliseconds(printDocumentChild.orderInfo.payTime);

                Label payTimes = new Label();
                payTimes.Text = "支付时间: " + dt1.ToString("yyyy/MM/dd HH:mm:ss");
                payTimes.Left = left;
                payTimes.Font = new Font("微软雅黑", 10);
                payTimes.ForeColor = Color.Gray;
                payTimes.Top = top + payTimes.Height + payNO.Top;
                p.Controls.Add(payTimes);

                Label u4 = new Label();
                u4.Text = "--------------------------------------------";
                u4.Left = left;
                u4.Font = new Font("微软雅黑", 10);
                u4.ForeColor = Color.Gray;
                u4.Top = top + u4.Height + payTimes.Top;
                p.Controls.Add(u4);

                Label address = new Label();
                address.Text = "地址: " + Login.userInfo.address;
                address.Left = left;
                address.Font = new Font("微软雅黑", 10);
                address.ForeColor = Color.Gray;
                address.Top = top + address.Height + u4.Top;
                p.Controls.Add(address);

                Label cusPhone = new Label();
                cusPhone.Text = "电话: " + Login.userInfo.cusPhone;
                cusPhone.Left = left;
                cusPhone.Font = new Font("微软雅黑", 10);
                cusPhone.ForeColor = Color.Gray;
                cusPhone.Top = top + cusPhone.Height + address.Top;
                p.Controls.Add(cusPhone);

                Label hello = new Label();
                hello.Text = "谢谢惠顾欢迎您下次光临";
                hello.Left = left + 70;
                hello.Font = new Font("微软雅黑", 10);
                hello.ForeColor = Color.Gray;
                hello.Top = top + hello.Height + cusPhone.Top;
                p.Controls.Add(hello);

            }
            else if (printDocumentChild.orderInfo.payStatus == 2)
            {
                Label u5 = new Label();
                u5.Text = "--------------------------------------------";
                u5.Left = left;
                u5.Font = new Font("微软雅黑", 10);
                u5.ForeColor = Color.Gray;
                u5.Top = top + u5.Height + memberMoney.Top;
                p.Controls.Add(u5);

                string payType = "挂账";

                Label payTypes = new Label();
                payTypes.Text = "支付方式: " + payType;
                payTypes.Left = left;
                payTypes.Font = new Font("微软雅黑", 10);
                payTypes.ForeColor = Color.Gray;
                payTypes.Top = top + payTypes.Height + u5.Top;
                p.Controls.Add(payTypes);

                System.DateTime payTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                DateTime dt1 = payTime.AddMilliseconds(printDocumentChild.orderInfo.payTime);

                Label payTimes = new Label();
                payTimes.Text = "挂账时间: " + dt1.ToString("yyyy/MM/dd HH:mm:ss");
                payTimes.Left = left;
                payTimes.Font = new Font("微软雅黑", 10);
                payTimes.ForeColor = Color.Gray;
                payTimes.Top = top + payTimes.Height + payTypes.Top;
                p.Controls.Add(payTimes);

                Label u4 = new Label();
                u4.Text = "--------------------------------------------";
                u4.Left = left;
                u4.Font = new Font("微软雅黑", 10);
                u4.ForeColor = Color.Gray;
                u4.Top = top + u4.Height + payTimes.Top;
                p.Controls.Add(u4);

                Label address = new Label();
                address.Text = "地址: " + Login.userInfo.address;
                address.Left = left;
                address.Font = new Font("微软雅黑", 10);
                address.ForeColor = Color.Gray;
                address.Top = top + address.Height + u4.Top;
                p.Controls.Add(address);

                Label cusPhone = new Label();
                cusPhone.Text = "电话: " + Login.userInfo.cusPhone;
                cusPhone.Left = left;
                cusPhone.Font = new Font("微软雅黑", 10);
                cusPhone.ForeColor = Color.Gray;
                cusPhone.Top = top + cusPhone.Height + address.Top;
                p.Controls.Add(cusPhone);

                Label hello = new Label();
                hello.Text = "谢谢惠顾欢迎您下次光临";
                hello.Left = left + 70;
                hello.Font = new Font("微软雅黑", 10);
                hello.ForeColor = Color.Gray;
                hello.Top = top + hello.Height + cusPhone.Top;
                p.Controls.Add(hello);

            }


            p.Controls.Add(shopName);
            p.Controls.Add(type);
            p.Controls.Add(u);
            
            p.Controls.Add(odinal);
            p.Controls.Add(printDate);
            p.Controls.Add(orderNo);
            p.Controls.Add(orderDate);
            p.Controls.Add(u2);


            foreach (Control c in p.Controls)
            {
                if (c.GetType() == typeof(Label))
                {
                    e.Graphics.DrawString(c.Text, c.Font, new SolidBrush(c.ForeColor), c.Location);
                }

            }

        }
        private void button1_Click(object sender, EventArgs e)
        {
            //  try
            //   {
                d.textBox1.Text = "桌台";
                d.textBox1.Font = new Font("微软雅黑", 14);
                d.textBox1.ForeColor = Color.Gray;
                d.orderId = 0;
                orderNo = null;
                d.deskState = -1;
                d.search.BackgroundImage = Properties.Resources.nullButtonState;
                d.button3.BackgroundImage = Properties.Resources.eatButtonState;
                d.desk_right_top.Visible = false;
                d.desk_right_bottom.Visible = false;
                d.desk_right_center.Controls.Clear();
                d.panel1.Controls.Clear();
                PictureBox picture = new PictureBox();
            picture.Size = new Size(183, 305);
            picture.Location = new Point(58, -14);
            picture.BackgroundImage = Properties.Resources.noDeatail;
                d.desk_right_center.Controls.Add(picture);
                this.payorder.BackgroundImage = Properties.Resources.payleave;
                this.order.BackgroundImage = Properties.Resources.orderleave;
                this.desk.BackgroundImage = Properties.Resources.desk;
                this.panelAll.Controls.Clear();

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };
                JObject json3 = new JObject { { "pageSize", 12 }, { "totalCount", 1 } };

                json2.Add("data", json3.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                Console.WriteLine("json2.ToString():" + json2.ToString());
                Console.WriteLine("result" + result);
                getDeskList(result);
                d.pageIndex.Text = "1";
           // } catch (Exception q) { MessageBox.Show(q.Message); }
        }
        public void getDeskList(string result)
        {
            if (result != string.Empty)
            {
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                if (jo["errorCode"].ToString().Equals("0"))
                {
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                    if (int.Parse(jsonObect["count"].ToString()) > 0)
                    {
                        d.count.Text = jsonObect["totalCount"].ToString();
                        d.pageIndex.Text = "1";
                        int page = int.Parse(jsonObect["totalCount"].ToString()) / 12;
                        double y = double.Parse(jsonObect["totalCount"].ToString()) % 12;
                        if (y > 0)
                        {
                            page += 1;
                        }
                        d.panel5.Visible = false;
                        d.page.Text = page.ToString();

                        JArray jarry = JArray.Parse(jsonObect["items"].ToString());
                        d.deskList = new List<DeskInfo>();
                        int j = 0;
                        int k = 0;
                        for (int i = 0; i < int.Parse(jsonObect["count"].ToString()); i++)
                        {
                            DeskInfo deskInfo = new DeskInfo();
                            deskInfo.ID = int.Parse(jarry[i]["ID"].ToString());
                            deskInfo.deskNO = jarry[i]["deskNO"].ToString();
                            deskInfo.deskName= jarry[i]["deskName"].ToString();
                            deskInfo.deskStatus = jarry[i]["deskStatus"].ToString();
                            if (int.Parse(jarry[i]["deskStatus"].ToString()) != 0)
                            {
                                if (jarry[i]["orderNO"] != null)
                                {                                   
                                    deskInfo.totalNum = int.Parse(jarry[i]["totalNum"].ToString());
                                    deskInfo.orderNO = jarry[i]["orderNO"].ToString();
                                    if (jarry[i]["saleMoney"]!=null)
                                    {
                                        deskInfo.saleMoney = double.Parse(jarry[i]["saleMoney"].ToString());

                                    }
                                   deskInfo.orderID= int.Parse(jarry[i]["orderID"].ToString());
                                    deskInfo.serviceStatus= int.Parse(jarry[i]["serviceStatus"].ToString());
                                    if (jarry[i]["payStatus"] != null)
                                    {
                                        deskInfo.payStatus = int.Parse(jarry[i]["payStatus"].ToString());
                                    }
                                }
                            }                           
                          
       
                            Label l1 = new Label();
                            l1.BackColor = Color.Transparent;
                            l1.Text = jarry[i]["deskNO"].ToString() + "|" + jarry[i]["deskName"].ToString();
                            l1.ForeColor = Color.FromArgb(255, 102, 102, 102);
                            l1.Top = 122;

                            // CheckBoxChild ck = new CheckBoxChild();
                            // ck.Location = new Point(10, 10);
                            // ck.Visible = false;                          
                            //  ck.BackColor = Color.Transparent;
                        
                            d.deskList.Add(deskInfo);
                            if (i <= 3)
                            {
                                PanelChild p = new PanelChild();
                                p.deskState = int.Parse(jarry[i]["deskStatus"].ToString());
                                if (int.Parse(jarry[i]["deskStatus"].ToString()) == 0)
                                {
                                    p.BackgroundImage = Properties.Resources.nullState;
                                }
                                else if(int.Parse(jarry[i]["deskStatus"].ToString()) == 1)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatState;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.eat_noservice;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                                    }
                                   

                                 
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        //         ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                    }
                                    
                                    p.Controls.Add(l2);

                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                   
                                   
                                   // price.Location = new Point(53, 80);
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();                                      
                                    }
                                    else
                                    {
                                        price.Text = "￥0" ;
                                    }
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 2)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatreserve;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.reserve_noserve;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                                    }

                                  
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        //ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                    }
                                   
                                    p.Controls.Add(l2);

                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                   
                                   // price.Location = new Point(53, 80);
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    price.ForeColor = Color.White;
                                    
                                    p.Controls.Add(price);

                                }
                                p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
                                p.Size = new Size(163, 140);
                                p.BackColor = Color.Transparent;
                                p.Location = new Point(1 + i * 163, 0);
                                l1.Left = (p.Width / 2) - (l1.Width / 2);
                                l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                p.Controls.Add(l1);
                                d.panel1.Controls.Add(p);
                                //  p.Controls.Add(ck);
                              
                                p.Click += new EventHandler(showDeskDeatail);
                              //  p.ck = ck;
                             //   p.MouseHover += new EventHandler(showEdit);
                      
                            } else if (i > 3 && i < 8)
                            {
                                PanelChild p = new PanelChild();
                                p.deskState = int.Parse(jarry[i]["deskStatus"].ToString());
                                if (int.Parse(jarry[i]["deskStatus"].ToString()) == 0)
                                {
                                    p.BackgroundImage = Properties.Resources.nullState;
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 1)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                       if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatState;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.eat_noservice;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                                    }

                                   
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                 //       ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                    }
                                   

                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                   // price.Location = new Point(53, 80);
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);

                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 2)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatreserve;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.reserve_noserve;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                                    }
                                   
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        //       ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                    }                                   

                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);

                                }

                                p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
                                p.Size = new Size(163, 140);
                                p.BackColor = Color.Transparent;
                             //   p.ck = ck;
                                p.Location = new Point(1 + j * 163, 141);
                                p.Controls.Add(l1);
                            //    p.Controls.Add(ck);
                                j++;                             
                                d.panel1.Controls.Add(p);
                                //    p.MouseHover+= new EventHandler(showEdit);
                                l1.Left = (p.Width / 2) - (l1.Width / 2);
                                l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                p.Controls.Add(l1);
                                p.Click += new EventHandler(showDeskDeatail);
                          
                            }
                            else if (i >= 8 && i < 12)
                            {
                                PanelChild p = new PanelChild();
                                p.deskState = int.Parse(jarry[i]["deskStatus"].ToString());
                                if (int.Parse(jarry[i]["deskStatus"].ToString()) == 0)
                                {
                                    p.BackgroundImage = Properties.Resources.nullState;
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 1)

                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatState;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.eat_noservice;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                                    }

                                   
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);

                                   
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                      //  ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                    }                                 

                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 2)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatreserve;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.reserve_noserve;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                                    }

                                  
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);

                                   
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        //  ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                    }

                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);
                                }
                                p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
                                p.Size = new Size(163, 140);
                                p.BackColor = Color.Transparent;
                           //     p.ck = ck;
                                p.Location = new Point(1 + k * 163, 282);
                                p.Controls.Add(l1);
                              //  p.Controls.Add(ck);
                                k++;
                                d.panel1.Controls.Add(p);
                                p.Click += new EventHandler(showDeskDeatail);
                                l1.Left = (p.Width / 2) - (l1.Width / 2);                               
                                l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                p.Controls.Add(l1);
                                //  p.MouseHover += new EventHandler(showEdit);

                            }
                            d.Show();
                            this.panelAll.Controls.Add(d);
                        }
                    }
                    else
                    {
                        d.pageIndex.Text = "0";
                        d.page.Text = "0";
                        d.count.Text = "0";
                        MessageBox.Show("暂无桌台");
                    }
                }
                else
                { MessageBox.Show(jo["message"].ToString()); }
            }

        }
        public void showEdit(object sender, EventArgs e)
        {
            if (removeArray[0] != null)
            {

               removeArray[0].BackColor = Color.FromArgb(255, 200, 200, 200);
                if (removeArray[0].ck.Checked!=true)
                {
                    removeArray[0].ck.Visible = false;
                    removeArray[0].BackColor = Color.Transparent;
                }
             
            }

            PanelChild panel = (PanelChild)sender;
            removeArray[0] = panel;
            panel.BackColor= Color.FromArgb(255, 200, 200, 200);
            panel.ck.Visible = true;
            
        }
        public static void showStEdit(object sender, EventArgs e)
        {
            if (removeArray[0] != null)
            {

                removeArray[0].BackColor = Color.FromArgb(255, 200, 200, 200);
                if (removeArray[0].ck.Checked != true)
                {
                    removeArray[0].ck.Visible = false;
                    removeArray[0].BackColor = Color.Transparent;
                }

            }

            PanelChild panel = (PanelChild)sender;
            removeArray[0] = panel;
            panel.BackColor = Color.FromArgb(255, 200, 200, 200);
            panel.ck.Visible = true;

        }

        public static void getDesks(string result,bool clearPageIndex)
        {
            
            if (result != string.Empty)
            {
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                if (jo["errorCode"].ToString().Equals("0"))
                {
                    if (clearPageIndex)
                    {
                        d.pageIndex.Text = "1";
                    }
                    orderNo = null;
                    d.desk_right_top.Visible = false;
                    d.desk_right_bottom.Visible = false;
                    d.desk_right_center.Controls.Clear();
                    d.panel1.Controls.Clear();
                    PictureBox picture = new PictureBox();
                    picture.Size = new Size(183, 305);
                    picture.Location = new Point(58, -14);
                    picture.BackgroundImage = Properties.Resources.noDeatail;
                    d.desk_right_center.Controls.Add(picture);

                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                    if (int.Parse(jsonObect["count"].ToString()) > 0)
                    {
                        d.count.Text = jsonObect["totalCount"].ToString();
                        
                        int page = int.Parse(jsonObect["totalCount"].ToString()) / 12;
                        double y = double.Parse(jsonObect["totalCount"].ToString()) % 12;
                        if (y > 0)
                        {
                            page += 1;
                        }

                        d.page.Text = page.ToString();
                        d.panel5.Visible = false;
                        JArray jarry = JArray.Parse(jsonObect["items"].ToString());
                        d.deskList = new List<DeskInfo>();
                        int j = 0;
                        int k = 0;
                        for (int i = 0; i < int.Parse(jsonObect["count"].ToString()); i++)
                        {
                            DeskInfo deskInfo = new DeskInfo();
                            deskInfo.ID = int.Parse(jarry[i]["ID"].ToString());
                            deskInfo.deskNO = jarry[i]["deskNO"].ToString();
                            deskInfo.deskName = jarry[i]["deskName"].ToString();
                            deskInfo.deskStatus = jarry[i]["deskStatus"].ToString();
                            if (int.Parse(jarry[i]["deskStatus"].ToString()) != 0)
                            {
                                if (jarry[i]["orderNO"] != null)
                                {
                                    deskInfo.totalNum = int.Parse(jarry[i]["totalNum"].ToString());
                                    deskInfo.orderNO = jarry[i]["orderNO"].ToString();
                                    deskInfo.orderID= int.Parse(jarry[i]["orderID"].ToString());
                                    if (jarry[i]["saleMoney"]!=null)
                                    {
                                        deskInfo.saleMoney = double.Parse(jarry[i]["saleMoney"].ToString());
                                    }
                                    
                                    deskInfo.serviceStatus = int.Parse(jarry[i]["serviceStatus"].ToString());
                                    if (jarry[i]["payStatus"]!=null)
                                    {
                                        deskInfo.payStatus = int.Parse(jarry[i]["payStatus"].ToString());
                                    }
                                }
                            }
                            d.deskList.Add(deskInfo);
                           
                            Label l1 = new Label();
                            l1.BackColor = Color.Transparent;
                            l1.Top = 122;
                            l1.Text = jarry[i]["deskNO"].ToString() + "|" + jarry[i]["deskName"].ToString();
                            l1.ForeColor = Color.FromArgb(255, 102, 102, 102);
 
                            //   CheckBoxChild ck = new CheckBoxChild();
                            //   ck.Location = new Point(10, 10);
                            //   ck.Visible = false;
                            //   ck.BackColor = Color.Transparent;

                            if (i <= 3)
                            {
                                PanelChild p = new PanelChild();
                                p.deskState = int.Parse(jarry[i]["deskStatus"].ToString());
                                if (int.Parse(jarry[i]["deskStatus"].ToString()) == 0)
                                {
                                    p.BackgroundImage = Properties.Resources.nullState;
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 1)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatState;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.eat_noservice;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                                    }

                                   
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        //            ck.orderNo= jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                   
                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 2)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatreserve;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.reserve_noserve;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                                    }

                                 
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        //            ck.orderNo= jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                   
                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);
                                }

                                p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
                                p.Size = new Size(163, 140);
                                p.BackColor = Color.Transparent;
                                p.Location = new Point(1 + i * 163, 0);
                                d.panel1.Controls.Add(p);
                           //     p.ck = ck;
                           //     p.MouseHover += new EventHandler(showStEdit);
                                p.Click += new EventHandler(showDeskDeatail);
                                l1.Left = (p.Width / 2) - (l1.Width / 2);
                                l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                p.Controls.Add(l1);
                            }
                            else if (i > 3 && i <8)
                            {
                                PanelChild p = new PanelChild();
                                p.deskState = int.Parse(jarry[i]["deskStatus"].ToString());
                                if (int.Parse(jarry[i]["deskStatus"].ToString()) == 0)
                                {
                                    p.BackgroundImage = Properties.Resources.nullState;
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 1)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatState;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.eat_noservice;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                                    }

                                   
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                           //             ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                    }
                                   
                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);

                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 2)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatreserve;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.reserve_noserve;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                                    }
                                   
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        //             ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                    }
                                   
                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);

                                }

                                p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
                                p.Size = new Size(163, 140);
                                p.BackColor = Color.Transparent;

                                p.Location = new Point(1 + j * 163, 141);
                                p.Controls.Add(l1);
                             //   p.ck = ck;
                             //   p.MouseHover += new EventHandler(showStEdit);
                                j++;
                                d.panel1.Controls.Add(p);
                                p.Click += new EventHandler(showDeskDeatail);
                                l1.Left = (p.Width / 2) - (l1.Width / 2);   
                                l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                p.Controls.Add(l1);
                            }
                            else if (i >= 8 && i < 12)
                            {
                                PanelChild p = new PanelChild();
                                p.deskState = int.Parse(jarry[i]["deskStatus"].ToString());
                                if (int.Parse(jarry[i]["deskStatus"].ToString()) == 0)
                                {
                                    p.BackgroundImage = Properties.Resources.nullState;
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 1)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatState;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.eat_noservice;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                                    }


                                  
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);

                                   
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                //        ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);
                                }
                                else if (int.Parse(jarry[i]["deskStatus"].ToString()) == 2)
                                {
                                    Label l2 = new Label();
                                    if (jarry[i]["payStatus"] != null && int.Parse(jarry[i]["payStatus"].ToString()) == 1)
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.ispaynoservice;
                                        }
                                        else
                                        {
                                            p.BackgroundImage = Properties.Resources.ispay;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 34, 198, 131);
                                    }
                                    else
                                    {
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 0)
                                        {
                                            p.BackgroundImage = Properties.Resources.eatreserve;
                                        }
                                        else if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == -1)
                                        {
                                            p.BackgroundImage = Properties.Resources.reserve_noserve;
                                        }
                                        l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                                    }


                                  
                                    l2.AutoSize = true;
                                    l2.Font = new Font("微软雅黑", 14);
                                    l2.BackColor = Color.Transparent;
                                    l2.Location = new Point(93, 18);

                                    
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        //        ck.orderNo = jarry[i]["orderNO"].ToString();
                                        l2.Text = jarry[i]["totalNum"].ToString();
                                        p.ID = int.Parse(jarry[i]["orderID"].ToString());
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                    p.Controls.Add(l2);
                                    Label price = new Label();
                                    price.BackColor = Color.Transparent;
                                    price.Top = 80;
                                    price.Left = 28;
                                    price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                    if (jarry[i]["saleMoney"] != null)
                                    {
                                        price.Text = "￥" + jarry[i]["saleMoney"].ToString();

                                    }
                                    else
                                    {
                                        price.Text = "￥0";
                                    }
                                    price.ForeColor = Color.White;
                                    p.Controls.Add(price);
                                }

                                p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
                                p.Size = new Size(163, 140);
                                p.BackColor = Color.Transparent;
                                p.Location = new Point(1 + k * 163, 282);
                                p.Controls.Add(l1);
                             //   p.ck = ck;
                             //   p.MouseHover += new EventHandler(showStEdit);
                                k++;
                                d.panel1.Controls.Add(p);
                                p.Click += new EventHandler(showDeskDeatail);
                                l1.Left = (p.Width / 2) - (l1.Width / 2);
                                l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                                p.Controls.Add(l1);
                            }
                            //d.Show();
                            //this.panelAll.Controls.Add(d);
                        }
                    

                    }
                    else
                    {
                        d.pageIndex.Text = "0";
                        d.page.Text = "0";
                        d.count.Text = "0";
                        MessageBox.Show("暂无桌台");
                    }
                }
                else
                { MessageBox.Show(jo["message"].ToString()); }
            }

        }
        public static void showDeskDeatail(object sender, EventArgs e)
        {

         //  try
         //   {
                if (panelArrayDesk[0] != null)
                {
                    panelArrayDesk[0].BackColor = Color.Transparent;
                }     
               
                PanelChild p = (PanelChild)sender;
                panelArrayDesk[0] = p;
                p.BackColor = Color.FromArgb(255, 200, 200, 200);
                if (p.deskState == 1||p.deskState == 2)
                {                  
                    if (p.orderNO != null)
                    {
                        d.desk_right_top.Visible = true;
                        d.desk_right_bottom.Visible = true;
                        d.desk_right_center.Controls.Clear();
                        d.desk_right_bottom.Controls.Clear();
                        d.orderId = p.ID;
                       
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 86 }, { "muuid", Login.userInfo.muuid } };
                        JObject json3 = new JObject { { "orderNO", p.orderNO } };

                        json2.Add("data", json3.ToString());

                        string result = string.Empty;
                        result = HttpPostData(testurl.ToString(), json2.ToString());
                        if (result != string.Empty)
                        {
                            Console.WriteLine("deskdedatil" + result);
                            JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                            if (jo["errorCode"].ToString().Equals("0"))
                            {
                                d.panel5.Visible = true;
                                JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                                orderNo = p.orderNO;
                                sellState = int.Parse(jsonObect["payStatus"].ToString());
                                if (jsonObect["deskName"].ToString().Length > 3)
                                {
                                    d.deskNo.Text = jsonObect["deskName"].ToString().Substring(0, 3) + "...";
                                }
                                else
                                {
                                    d.deskNo.Text = jsonObect["deskName"].ToString();
                                }
                                d.deskName = jsonObect["deskName"].ToString();
                                if (jsonObect["primeMoney"]!=null)
                                {
                                    d.price.Text = jsonObect["primeMoney"].ToString();
                                }
                              

                                d.orderTime.Text = getTime(long.Parse(jsonObect["createTime"].ToString()));
                                if (jsonObect["totalNum"] != null && jsonObect["totalNum"].ToString().Equals("0"))
                                {
                                    d.eatPeoCount.Text = "未填人数";
                                }
                                else
                                {
                                    d.eatPeoCount.Text = jsonObect["totalNum"].ToString();
                                }
                               

                                int left = 10;
                                Label l1 = new Label();
                                l1.Size = new Size(100, 20);
                                if (int.Parse(jsonObect["serviceStatus"].ToString()) == -1)
                                {
                                    l1.Name = "未服务";
                                    l1.ForeColor = Color.Gray;
                                }
                                else if (int.Parse(jsonObect["serviceStatus"].ToString()) == 0)
                                {
                                    l1.Name = "服务中";
                                    l1.ForeColor = Color.Gray;
                                }
                                else if (int.Parse(jsonObect["serviceStatus"].ToString()) == 1)
                                {
                                    l1.Name = "服务完成";
                                    l1.ForeColor = Color.Red;
                                }
                                else if (int.Parse(jsonObect["serviceStatus"].ToString()) == 2)
                                {
                                    l1.Name = "订单取消";
                                    l1.ForeColor = Color.Red;
                                }

                                l1.Text = l1.Name;
                                l1.Font = new Font("微软雅黑", 10);

                                l1.Location = new Point(20, 20);
                                d.desk_right_center.Controls.Add(l1);

                                Label vipPrice = new Label();
                                vipPrice.AutoSize = true;
                                vipPrice.Size = new Size(100, 20);
                            if (jsonObect["vipMoney"]!=null)
                            {
                                vipPrice.Name = "会员价：￥" + jsonObect["vipMoney"].ToString();
                            }
                                
                                vipPrice.Text = vipPrice.Name;
                                vipPrice.Font = new Font("微软雅黑", 10);
                                vipPrice.ForeColor = Color.Gray;
                                vipPrice.Location = new Point(20, 40);
                                d.desk_right_center.Controls.Add(vipPrice);

                                Label orderNO = new Label();
                                orderNO.AutoSize = true;
                                orderNO.Size = new Size(100, 20);
                                orderNO.Name = "订单编号：" + jsonObect["orderNO"].ToString();
                                orderNO.Text = orderNO.Name;
                                orderNO.Location = new Point(20, 60);
                                orderNO.Font = new Font("微软雅黑", 10);
                                orderNO.ForeColor = Color.Gray;

                                d.desk_right_center.Controls.Add(orderNO);

                                Label orderDeatail = new Label();
                                orderDeatail.Size = new Size(100, 20);
                                orderDeatail.Name = "订单详情";
                                orderDeatail.Text = orderDeatail.Name;
                                orderDeatail.Font = new Font("微软雅黑", 10);
                                orderDeatail.ForeColor = Color.Gray;

                                d.desk_right_center.Controls.Add(orderDeatail);

                                ButtonChild print = new ButtonChild();
                                print.FlatStyle = FlatStyle.Flat;
                                print.FlatAppearance.BorderSize = 0;
                                print.Size = new Size(58, 26);

                                print.BackgroundImage = Properties.Resources.print;
                                print.Click += new EventHandler(printOrderDetail);
                                print.orderNo = jsonObect["orderNO"].ToString(); ;

                                d.desk_right_center.Controls.Add(print);

                                ButtonChild printAgin = new ButtonChild();
                                printAgin.FlatStyle = FlatStyle.Flat;
                                printAgin.FlatAppearance.BorderSize = 0;
                                printAgin.Size = new Size(58, 26);

                                printAgin.BackgroundImage = Properties.Resources.printAgin;
                                printAgin.Click += new EventHandler(printAginOrderDetail);

                                printAgin.orderNo = jsonObect["orderNO"].ToString();
                                d.desk_right_center.Controls.Add(printAgin);
                                Label l5 = new Label();
                                l5.Size = new Size(100, 20);


                                l5.Font = new Font("微软雅黑", 10);
                                l5.ForeColor = Color.Gray;
                                l5.Location = new Point(160, 20);
                                d.desk_right_center.Controls.Add(l5);

                                ButtonChild pay = new ButtonChild();
                                pay.FlatStyle = FlatStyle.Flat;
                                pay.FlatAppearance.BorderSize = 0;
                                pay.Size = new Size(94, 26);
                                pay.Location = new Point(33, 8);
                                if (jsonObect["primeMoney"] != null)
                                {
                                    pay.handlePrice = double.Parse(jsonObect["primeMoney"].ToString());
                                }
                                if (jsonObect["vipMoney"] != null)
                                {
                                    pay.reducedPrice = double.Parse(jsonObect["vipMoney"].ToString());
                                }
                                if (jsonObect["orderNO"] != null)
                                {
                                    pay.orderNo = jsonObect["orderNO"].ToString();
                                    pay.orderId = int.Parse(jsonObect["ID"].ToString());
                                }

                                ButtonChild refund = new ButtonChild();
                                refund.FlatStyle = FlatStyle.Flat;
                                refund.FlatAppearance.BorderSize = 0;
                                refund.Size = new Size(94, 26);
                                refund.Location = new Point(163, 8);

                                if (jsonObect["paidMoney"] != null)
                                {
                                    pay.realityPrice = double.Parse(jsonObect["paidMoney"].ToString());

                                    refund.realityPrice = double.Parse(jsonObect["paidMoney"].ToString());
                                }
                                if (jsonObect["orderNO"] != null)
                                {
                                    refund.orderNo = jsonObect["orderNO"].ToString();
                                }

                                if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 0)
                                {
                                    l5.Name = "未支付";
                                    pay.BackgroundImage = Properties.Resources.payButton;
                                    refund.BackgroundImage = Properties.Resources.refundButton;
                                    pay.Click += new EventHandler(payShow);

                                }
                                else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 2)
                                {
                                    l5.Name = "记账";
                                    pay.BackgroundImage = Properties.Resources.payButton;
                                    refund.BackgroundImage = Properties.Resources.refundButton;
                                    pay.Click += new EventHandler(payShow);
                                }

                                else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 5)
                                {
                                    l5.Name = "支付失败";
                                    pay.BackgroundImage = Properties.Resources.payButton;
                                    refund.BackgroundImage = Properties.Resources.refundButton;
                                    pay.Click += new EventHandler(payShow);
                                }
                                else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 6)
                                {
                                    l5.Name = "支付中";
                                    pay.BackgroundImage = Properties.Resources.payButton;
                                    refund.BackgroundImage = Properties.Resources.refundButton;
                                    pay.Click += new EventHandler(payShow);
                                }
                                else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 7)
                                {
                                    l5.Name = "退款中";
                                    pay.BackgroundImage = Properties.Resources.payButton;
                                    refund.BackgroundImage = Properties.Resources.refundButton;
                                    pay.Click += new EventHandler(payShow);
                                }
                                else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 8)
                                {
                                    l5.Name = "退款失败";
                                    pay.BackgroundImage = Properties.Resources.payButton;
                                    refund.BackgroundImage = Properties.Resources.refundButton;
                                    pay.Click += new EventHandler(payShow);
                                    refund.Click += new EventHandler(refundMoney);
                                }
                                else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 9)
                                {
                                    l5.Name = "已退款";
                                    refund.BackgroundImage = Properties.Resources.refundButtonClick;
                                    pay.Click += new EventHandler(payShow);
                                    pay.BackgroundImage = Properties.Resources.payButton;
                                }
                                else if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) == 10)
                                {
                                    l5.Name = "订单改变";
                                    refund.BackgroundImage = Properties.Resources.refundButton;    
                                    pay.Click += new EventHandler(payShow);
                                    pay.BackgroundImage = Properties.Resources.payButton;
                                   
                                }
                                else
                                {
                                    pay.BackgroundImage = Properties.Resources.payButtonClick;
                                    refund.BackgroundImage = Properties.Resources.refundButton;
                                    if (jsonObect["payNO"] != null)
                                    {
                                        refund.payNo = jsonObect["payNO"].ToString();
                                        refund.Click += new EventHandler(refundMoney);
                                    }



                                    if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 1)
                                    {
                                        l5.Name = "微信支付";
                                    }
                                    else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 2)
                                    {
                                        l5.Name = "支付宝支付";
                                    }
                                    else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 3)
                                    {
                                        l5.Name = "现金支付";
                                    }
                                    else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 4)
                                    {
                                        l5.Name = "银行卡支付";
                                    }
                                    else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 5)
                                    {
                                        l5.Name = "其他支付";
                                    }
                                    else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 10)
                                    {
                                        l5.Name = "会员卡支付";
                                    }
                                    else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 12)
                                    {
                                        l5.Name = "美团支付";
                                    }
                                    else if (jsonObect["payType"] != null && int.Parse(jsonObect["payType"].ToString()) == 13)
                                    {
                                        l5.Name = "饿了么支付";
                                    }

                            }

                                d.desk_right_bottom.Controls.Add(pay);
                                d.desk_right_bottom.Controls.Add(refund);

                                l5.Text = l5.Name;
                                Label cpName = new Label();
                                // cpName.Size = new Size(20, 20);
                                cpName.Name = "名称";
                                cpName.Text = cpName.Name;

                                cpName.Font = new Font("微软雅黑", 10);
                                cpName.ForeColor = Color.Gray;

                                Label price = new Label();
                                //  price.Size = new Size(20, 20);
                                price.Name = "价格/会员价格";
                                price.Text = price.Name;

                                price.Font = new Font("微软雅黑", 10);
                                price.ForeColor = Color.Gray;

                                Label count = new Label();
                                // count.Size = new Size(20, 20);
                                count.Name = "数量";
                                count.Text = count.Name;

                                count.Font = new Font("微软雅黑", 10);
                                count.ForeColor = Color.Gray;

                                d.desk_right_center.Controls.Add(cpName);
                                d.desk_right_center.Controls.Add(price);
                                d.desk_right_center.Controls.Add(count);
                                if (jsonObect["payStatus"] != null && int.Parse(jsonObect["payStatus"].ToString()) != 0 )
                                {


                                    Label payMoney = new Label();
                                payMoney.AutoSize = true;
                                payMoney.Size = new Size(100, 20);
                                    if (jsonObect["paidMoney"] != null)
                                    {
                                        payMoney.Name = "实收：￥" + jsonObect["paidMoney"].ToString();
                                    }
                                    payMoney.Text = payMoney.Name;
                                    payMoney.Font = new Font("微软雅黑", 10);
                                    payMoney.ForeColor = Color.Gray;
                                    payMoney.Location = new Point(160, 40);
                                    d.desk_right_center.Controls.Add(payMoney);


                                    Label payNO = new Label();
                                    payNO.AutoSize = true;
                                    payNO.Size = new Size(100, 20);
                                    if (jsonObect["payNO"] != null)
                                    {
                                        payNO.Name = "支付号：" + jsonObect["payNO"].ToString();
                                    }
                                    else
                                    {
                                    payNO.Name = "支付号：";
                                    }
                                   
                                    payNO.Text = payNO.Name;
                                    payNO.Font = new Font("微软雅黑", 10);
                                    payNO.ForeColor = Color.Gray;
                                    payNO.Location = new Point(20, 80);
                                    d.desk_right_center.Controls.Add(payNO);


                                    Label payTime = new Label();
                                    payTime.AutoSize = true;
                                    payTime.Size = new Size(100, 20);
                                if (jsonObect["payTime"] != null)
                                {
                                    payTime.Name = "支付时间：" + getTime(long.Parse(jsonObect["payTime"].ToString()));
                                }
                                else
                                {
                                    payTime.Name = "支付时间：" ;
                                }
                                   
                                    payTime.Text = payTime.Name;
                                    payTime.Font = new Font("微软雅黑", 10);
                                    payTime.ForeColor = Color.Gray;
                                    payTime.Location = new Point(20, 100);
                                    d.desk_right_center.Controls.Add(payTime);

                                    orderDeatail.Location = new Point(20, 130);
                                    print.Location = new Point(227, 125);
                                    printAgin.Location = new Point(160, 125);
                                    cpName.Location = new Point(20, 150);
                                    price.Location = new Point(151, 150);
                                    count.Location = new Point(250, 150);

                                    JArray attachedArray = new JArray();
                                    if (jsonObect["attached"] != null)
                                    {
                                        attachedArray = JArray.Parse(jsonObect["attached"].ToString());
                                    }
                                    if (attachedArray.Count > 0)
                                    {
                                        for (int i = 0; i < attachedArray.Count; i++)
                                        {
                                            Label cp = new Label();
                                            cp.AutoSize = true;
                                            cp.Size = new Size(125, 20);
                                            cp.Name = attachedArray[i]["name"].ToString();


                                            cp.Left = left;
                                            if (attachedArray[i]["delFlag"] != null && attachedArray[i]["delFlag"].ToString().Equals("-1"))
                                            {
                                                cp.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                                            }
                                            else
                                            {
                                                cp.Font = new Font("微软雅黑", 10);
                                            }
                                            cp.ForeColor = Color.Gray;
                                            cp.Location = new Point(20, 170 + i * 20);

                                            if (attachedArray[i]["attrs"] != null)
                                            {
                                                cp.Name += "(";
                                                JArray attrsArray = JArray.Parse(attachedArray[i]["attrs"].ToString());
                                                for (int j = 0; j < attrsArray.Count; j++)
                                                {

                                                    if (j == attrsArray.Count - 1)
                                                    {
                                                        cp.Name += attrsArray[j]["text"].ToString();
                                                    }
                                                    else
                                                    {
                                                        cp.Name += attrsArray[j]["text"].ToString() + ",";
                                                    }
                                                }
                                                cp.Name += ")";
                                            }
                                            cp.Text = cp.Name;

                                            Label cpPrice = new Label();
                                            // count.Size = new Size(20, 20);

                                            if (attachedArray[i]["vipPrice"] == null)
                                            {
                                                cpPrice.Name = "￥" + attachedArray[i]["price"].ToString();
                                            }
                                            else
                                            {
                                                cpPrice.Name = "￥" + attachedArray[i]["price"].ToString() + "/￥" + attachedArray[i]["vipPrice"].ToString();
                                            }
                                            cpPrice.Text = cpPrice.Name;
                                            cpPrice.Left = left;
                                            cpPrice.Font = new Font("微软雅黑", 10);
                                            cpPrice.ForeColor = Color.Gray;
                                            cpPrice.Location = new Point(151, 170 + i * 20);

                                            Label cpCount = new Label();
                                            // count.Size = new Size(20, 20);
                                            cpCount.Name = attachedArray[i]["count"].ToString();
                                            cpCount.Text = cpCount.Name;
                                            cpCount.Left = left;
                                            cpCount.Font = new Font("微软雅黑", 10);
                                            cpCount.ForeColor = Color.Gray;
                                            cpCount.Location = new Point(250, 170 + i * 20);
                                            d.desk_right_center.Controls.Add(cp);
                                            d.desk_right_center.Controls.Add(cpPrice);
                                            d.desk_right_center.Controls.Add(cpCount);
                                        }
                                    }
                                }
                                else
                                {
                                    orderDeatail.Location = new Point(20, 90);
                                    print.Location = new Point(227, 85);
                                    printAgin.Location = new Point(160, 85);
                                    cpName.Location = new Point(20, 110);
                                    price.Location = new Point(151, 110);
                                    count.Location = new Point(250, 110);

                                    JArray attachedArray = new JArray();
                                    if (jsonObect["attached"] != null)
                                    {
                                        attachedArray = JArray.Parse(jsonObect["attached"].ToString());
                                    }
                                    if (attachedArray.Count > 0)
                                    {
                                        for (int i = 0; i < attachedArray.Count; i++)
                                        {
                                            Label cp = new Label();
                                            cp.AutoSize = true;
                                            cp.Size = new Size(125, 20);
                                            cp.Name = attachedArray[i]["name"].ToString();


                                            cp.Left = left;
                                            if (attachedArray[i]["delFlag"] != null && attachedArray[i]["delFlag"].ToString().Equals("-1"))
                                            {
                                                cp.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                                            }
                                            else
                                            {
                                                cp.Font = new Font("微软雅黑", 10);
                                            }
                                            cp.ForeColor = Color.Gray;
                                            cp.Location = new Point(20, 130 + i * 20);

                                            if (attachedArray[i]["attrs"] != null)
                                            {
                                                cp.Name += "(";
                                                JArray attrsArray = JArray.Parse(attachedArray[i]["attrs"].ToString());
                                                for (int j = 0; j < attrsArray.Count; j++)
                                                {

                                                    if (j == attrsArray.Count - 1)
                                                    {
                                                        cp.Name += attrsArray[j]["text"].ToString();
                                                    }
                                                    else
                                                    {
                                                        cp.Name += attrsArray[j]["text"].ToString() + ",";
                                                    }
                                                }
                                                cp.Name += ")";
                                            }
                                            cp.Text = cp.Name;

                                            Label cpPrice = new Label();
                                            // count.Size = new Size(20, 20);

                                            if (attachedArray[i]["vipPrice"] == null)
                                            {
                                                cpPrice.Name = "￥" + attachedArray[i]["price"].ToString();
                                            }
                                            else
                                            {
                                                cpPrice.Name = "￥" + attachedArray[i]["price"].ToString() + "/￥" + attachedArray[i]["vipPrice"].ToString();
                                            }
                                            cpPrice.Text = cpPrice.Name;
                                            cpPrice.Left = left;
                                            cpPrice.Font = new Font("微软雅黑", 10);
                                            cpPrice.ForeColor = Color.Gray;
                                            cpPrice.Location = new Point(151, 130 + i * 20);

                                            Label cpCount = new Label();
                                            // count.Size = new Size(20, 20);
                                            cpCount.Name = attachedArray[i]["count"].ToString();
                                            cpCount.Text = cpCount.Name;
                                            cpCount.Left = left;
                                            cpCount.Font = new Font("微软雅黑", 10);
                                            cpCount.ForeColor = Color.Gray;
                                            cpCount.Location = new Point(250, 130 + i * 20);
                                            d.desk_right_center.Controls.Add(cp);
                                            d.desk_right_center.Controls.Add(cpPrice);
                                            d.desk_right_center.Controls.Add(cpCount);
                                        }
                                    }
                                }


                            }
                            else
                            {
                                MessageBox.Show(jo["message"].ToString());
                            }

                        }
                    }
                    else
                    {
                        MessageBox.Show("服务器异常：无订单号");
                    }
                   
                }
                else
                {
                    //空桌
                    d.desk_right_top.Visible = false;
                    d.desk_right_bottom.Visible = false;
                    orderNo = null;
                    d.panel5.Visible = false;
                    d.desk_right_center.Controls.Clear();
                    d.desk_right_bottom.Controls.Clear();
                    PictureBox picture = new PictureBox();
                    picture.Size = new Size(183, 305);
                    picture.Location = new Point(58, -14);
                    picture.BackgroundImage = Properties.Resources.noDeatail;
                    d.desk_right_center.Controls.Add(picture);
                }
                   

        //   }
       //   catch (Exception q) { MessageBox.Show(q.Message); }

        }

        public static string getTime(long time)
        {
            System.DateTime orderTIME = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
            DateTime dt = orderTIME.AddMilliseconds(time);
            return dt.ToString("yyyy/MM/dd HH:mm:ss");
        }
        public static void refundMoney(object sender, EventArgs e)
        {
            try
            {
                string str = Interaction.InputBox("请输入退款密码", "退款", "", -1, -1);
                if (str.Length>0)

                {
                    ButtonChild button = (ButtonChild)sender;
                    //退款密码接口
                        JObject json211 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 29 }, { "token", Login.userInfo.token } };
                        JObject json311 = new JObject { { "orderNO", button.orderNo } , { "refundPwd",str } };

                        json211.Add("data", json311.ToString());

                        string result3 = string.Empty;
                        result3 = HttpPostData(testurl.ToString(), json211.ToString());
                        if (result3 != string.Empty)
                        {
                            JObject jo = (JObject)JsonConvert.DeserializeObject(result3);
                            if (jo["errorCode"].ToString().Equals("0"))
                            {
                               
                                // if (button.payNo != null)
                                if (button.orderNo != null)
                                {

                                    JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 91 }, { "muuid", Login.userInfo.muuid } };
                                    JObject json3 = new JObject { { "orderNO", button.orderNo } };

                                    json2.Add("data", json3.ToString());

                                    string result2 = string.Empty;
                                    result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());


                                    string url = "http://pay.jqepay.com/refund/atonce";
                                    string result = string.Empty;
                                    string param = string.Format("orderNO={0}&muuid={1}&refundAmt={2}", button.orderNo, Login.userInfo.muuid, button.realityPrice);
                                    Console.WriteLine(param);
                                    result = MainForm.HttpPostData(url, param);
                                    if (result != string.Empty)
                                    {
                                        JObject jo2 = (JObject)JsonConvert.DeserializeObject(result);
                                        if (jo2["errorCode"].ToString().Equals("0"))
                                        {
                                            MainForm.printRefundCp(result2, button.realityPrice);
                                            MessageBox.Show("退款成功");
                                            d.textBox1.Text = "桌台";
                                            d.textBox1.Font = new Font("微软雅黑", 14);
                                            d.textBox1.ForeColor = Color.Gray;
                                            d.orderId = 0;
                                            orderNo = null;
                                            d.deskState = -1;
                                            d.search.BackgroundImage = Properties.Resources.nullButtonState;
                                            d.button3.BackgroundImage = Properties.Resources.eatButtonState;
                                            d.desk_right_top.Visible = false;
                                            d.desk_right_bottom.Visible = false;
                                            d.desk_right_center.Controls.Clear();
                                            d.panel1.Controls.Clear();
                                            PictureBox picture = new PictureBox();
                                            picture.Size = new Size(183, 305);
                                            picture.Location = new Point(58, -14);
                                            picture.BackgroundImage = Properties.Resources.noDeatail;
                                            d.desk_right_center.Controls.Add(picture);

                                            JObject json21 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };
                                            JObject json31 = new JObject { { "pageSize", 12 }, { "totalCount", 1 } };

                                            json21.Add("data", json31.ToString());
                                            string result1 = string.Empty;
                                            result1 = MainForm.HttpPostData(MainForm.testurl.ToString(), json21.ToString());
                                            Console.WriteLine("json21.ToString():" + json21.ToString());
                                            Console.WriteLine("result" + result1);
                                            getDesks(result1, true);

                                        }
                                        else
                                        {
                                            MessageBox.Show(jo["message"].ToString());
                                        }
                                    }
                                }
                                else
                                {
                                    MessageBox.Show("退款：订单号为空");
                                }
                        }
                        else
                        {
                            MessageBox.Show("密码错误");
                        }
                    }                          
                    
                }
                
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        private void isClose(int isclose)
        {
            try
            {
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 51 }, { "token", Login.userInfo.token } };
                JObject json3 = null;
                if (isclose == 1)
                {
                    json3 = new JObject { { "isClose", 0 }, { "ID", Login.userInfo.id } };
                }
                else
                {
                    json3 = new JObject { { "isClose", 1 }, { "ID", Login.userInfo.id } };
                }

                json2.Add("data", json3.ToString());

                string result = string.Empty;
                result = HttpPostData(testurl.ToString(), json2.ToString());
                if (result != string.Empty)
                {
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                    if (jo["errorCode"].ToString().Equals("0"))
                    {

                        if (isclose == 1)
                        {
                            Login.userInfo.isClose = 0;
                            this.button1.BackgroundImage = Properties.Resources.isopen;
                        }
                        else
                        {
                            Login.userInfo.isClose = 1;
                            this.button1.BackgroundImage = Properties.Resources.isclose;
                        }

                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                    }
                }
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        private void button1_Click_1(object sender, EventArgs e)
        {
            isClose(Login.userInfo.isClose);
        }

        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (e.CloseReason == CloseReason.WindowsShutDown)
            {
                isClose(0);
            }
        }
        public static void getReserves(string param,int type)
        {
            string result = string.Empty;
            result = HttpPostData(testurl.ToString(), param);
            if (result != string.Empty)
            {
                Console.WriteLine(result);
                if (result != null)
                {
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                    if (jo["errorCode"].ToString().Equals("0"))
                    {

                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                        if (int.Parse(jsonObect["count"].ToString()) > 0)
                        {

                            o.count.Text = jsonObect["totalCount"].ToString();
                            
                            int page = int.Parse(jsonObect["totalCount"].ToString()) / 9;
                            double y = double.Parse(jsonObect["totalCount"].ToString()) % 9;
                            if (y > 0)
                            {
                                page += 1;
                            }
                           
                            o.page.Text = page.ToString();
                            JArray jarry = JArray.Parse(jsonObect["items"].ToString());

                            int top = 2;
                            int left = 6;
                            int j = 0;
                            int k = 0;
                            o.orderPanel.Controls.Clear();
                            for (int i = 0; i < jarry.Count; i++)
                            {
                                Label l1 = new Label();
                                l1.BackColor = Color.Transparent;
                                if (jarry[i]["userName"] != null)
                                {
                                    l1.Name = "姓名：" + jarry[i]["userName"].ToString();
                                    l1.AutoSize = true;
                                    l1.Text = l1.Name;
                                    l1.Left = left;
                                    l1.Top = top + l1.Height;
                                    l1.Font = new Font("微软雅黑", 14);
                                    l1.ForeColor = Color.FromArgb(255, 51, 51, 51);
                                }
                             

                                Label lblTitle = new Label();
                                lblTitle.BackColor = Color.Transparent;
                                lblTitle.AutoSize = true;
                                if (jarry[i]["attendTime"] == null)
                                {
                                    MessageBox.Show("服务器异常：到餐时间为空");
                                    lblTitle.Name = "";
                                }
                                else
                                {
                                    System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                                    DateTime dt = startTime.AddMilliseconds(long.Parse(jarry[i]["attendTime"].ToString()));

                                    lblTitle.Name = "就餐：" + dt.ToString("yyyy/MM/dd HH:mm:ss");
                                }


                                lblTitle.Text = lblTitle.Name;
                                lblTitle.Left = left;
                                lblTitle.Font = new Font("微软雅黑", 10);
                                lblTitle.ForeColor = Color.FromArgb(255, 51, 51, 51);
                                lblTitle.Top = 10 + lblTitle.Height + l1.Top;

                                Label lblTitle2 = new Label();
                                lblTitle2.BackColor = Color.Transparent;
                                lblTitle2.AutoSize = true;
                                if (jarry[i]["phone"] == null)
                                {
                                    MessageBox.Show("服务器异常：手机号为空");
                                    lblTitle2.Name = "";
                                }
                                else
                                {
                                   lblTitle2.Name = "联系方式：" + jarry[i]["phone"].ToString();                                    
                                }


                                lblTitle2.Font = new Font("微软雅黑", 10);
                                lblTitle2.ForeColor = Color.FromArgb(255, 51, 51, 51);
                                lblTitle2.Text = lblTitle2.Name;


                                lblTitle2.Left = left;
                                lblTitle2.Top = top + lblTitle2.Height + lblTitle.Top;

                                Label lblTitle3 = new Label();
                                lblTitle3.BackColor = Color.Transparent;
                                lblTitle3.AutoSize = true;
                                if (jarry[i]["totalNum"] == null)
                                {
                                    MessageBox.Show("服务器异常：人数为空");
                                    lblTitle3.Name = "人数";
                                }
                                else
                                {
                                    lblTitle3.Name = "人数：" + jarry[i]["totalNum"].ToString();
                                }

                                lblTitle3.Font = new Font("微软雅黑", 10);
                                lblTitle3.ForeColor = Color.FromArgb(255, 51, 51, 51);
                                lblTitle3.Text = lblTitle3.Name;
                                lblTitle3.Left = left;
                                lblTitle3.Top = top + lblTitle3.Height + lblTitle2.Top;


                                Button remove = new Button();
                                remove.FlatStyle = FlatStyle.Flat;
                                remove.FlatAppearance.BorderSize = 0;
                                remove.Size = new Size(15, 15);

                                //remove.Text = "✘";
                                remove.Font = new Font("宋体", 14);
                                remove.Location = new Point(174, 0);


                                ButtonChild pay = new ButtonChild();
                                pay.FlatStyle = FlatStyle.Flat;
                                pay.FlatAppearance.BorderSize = 0;
                                pay.Size = new Size(50, 20);
                                pay.Click += new EventHandler(showAllocationDesk);
                               
                                if (jarry[i]["orderNO"] != null)
                                {
                                    pay.orderNo = jarry[i]["orderNO"].ToString();
                                }
                                if (jarry[i]["orderID"] != null)
                                {
                                    pay.orderId = int.Parse(jarry[i]["orderID"].ToString());
                                }
                                if (jarry[i]["deskNO"] == null)
                                {
                                    pay.BackgroundImage = Properties.Resources.allocation;
                                    pay.Size = new Size(50, 20);
                                    pay.Location = new Point(115, 108);
                                   
                                }
                                if (i >= 3 && i < 6)
                                {
                                    remove.BackgroundImage = Properties.Resources.remove2;
                                    PanelChild p = new PanelChild();
                                    p.Size = new Size(216, 135);
                                    p.BackColor = Color.FromArgb(255, 255, 255, 255);
                                    p.Location = new Point(1 + j * 216, 136);
                                    p.Controls.Add(l1);
                                //    p.Controls.Add(remove);
                                    p.Controls.Add(lblTitle);
                                    p.Controls.Add(lblTitle3);
                                    p.Controls.Add(lblTitle2);
                                    p.location = i;
                                    if (type == 1)//预定分配桌台
                                    {
                                        p.Controls.Add(pay);
                                        p.Click += new EventHandler(showDeatail);
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 1)
                                        {
                                            p.BackgroundImage = Properties.Resources.servicefinsh;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                        else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                        {
                                            p.BackgroundImage = Properties.Resources.orderCancel;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
                                            pay.Visible = false;
                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                    }
                                    else if (type == 0)
                                    {
                                        p.Click += new EventHandler(showReserveDeatail);
                                    }
                                    
                                    j++;

                                    o.orderPanel.Controls.Add(p);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                    
                                   
                                }
                                else if (i >= 6)
                                {
                                    remove.BackgroundImage = Properties.Resources.remove;
                                    PanelChild p = new PanelChild();
                                    p.Size = new Size(216, 135);
                                    p.BackColor = Color.FromArgb(255, 221, 221, 221);
                                    p.Location = new Point(1 + k * 216, 272);
                                    p.Controls.Add(l1);
                                //    p.Controls.Add(remove);
                                    p.location = i;
                                    p.Controls.Add(lblTitle);
                                    p.Controls.Add(lblTitle3);
                                    p.Controls.Add(lblTitle2);
                                    p.location = i;
                                    if (type == 1)//预定分配桌台
                                    {
                                        p.Controls.Add(pay);
                                        p.Click += new EventHandler(showDeatail);
                                        if (jarry[i]["serviceStatus"] != null && int.Parse(jarry[i]["serviceStatus"].ToString()) == 1)
                                        {
                                            p.BackgroundImage = Properties.Resources.servicefinsh;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                        else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                        {
                                            p.BackgroundImage = Properties.Resources.orderCancel;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
                                            pay.Visible = false;
                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                    }
                                    else if (type == 0)
                                    {
                                        p.Click += new EventHandler(showReserveDeatail);
                                    }
                                    k++;

                                    o.orderPanel.Controls.Add(p);
                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }

                                }
                                else
                                {
                                    remove.BackgroundImage = Properties.Resources.remove;
                                    PanelChild p = new PanelChild();
                                    p.Size = new Size(216, 135);
                                    p.BackColor = Color.FromArgb(255, 221, 221, 221);
                                    p.Location = new Point(1 + i * 216, 0);
                                    p.Controls.Add(l1);
                               //     p.Controls.Add(remove);
                                    p.Controls.Add(lblTitle);
                                    p.Controls.Add(lblTitle3);
                                    p.Controls.Add(lblTitle2);                                 

                                    o.orderPanel.Controls.Add(p);

                                    p.orderNO = jarry[i]["orderNO"].ToString();
                                    p.ID = int.Parse(jarry[i]["ID"].ToString());
                                    p.createTime = long.Parse(jarry[i]["createTime"].ToString());

                                    if (jarry[i]["orderNO"] != null)
                                    {
                                        p.orderNO = jarry[i]["orderNO"].ToString();
                                    }
                                    if (type == 1)//预定分配桌台
                                    {
                                        p.Controls.Add(pay);
                                        p.Click += new EventHandler(showDeatail);
                                        if (jarry[i]["serviceStatus"]!=null&&int.Parse(jarry[i]["serviceStatus"].ToString()) == 1)
                                        {
                                            p.BackgroundImage = Properties.Resources.servicefinsh;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;

                                            p.RightToLeft = RightToLeft.Yes;
                                        }else if (int.Parse(jarry[i]["serviceStatus"].ToString()) == 2)
                                        {
                                            p.BackgroundImage = Properties.Resources.orderCancel;

                                            p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
                                            pay.Visible = false;
                                            p.RightToLeft = RightToLeft.Yes;
                                        }
                                    }
                                    else if (type == 0)
                                    {
                                        p.Click += new EventHandler(showReserveDeatail);
                                    }
                                }
                            }
                        }
                        else
                        {
                            o.orderPanel.Controls.Clear();
                            o.pageIndex.Text = "0";
                            o.count.Text = "0";
                            o.page.Text = "0";
                            MessageBox.Show("暂无记录");
                        }


                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());

                    }
                }

            }
        }

        public static void showAllocationDesk(object sender, EventArgs e)
        {
            ButtonChild button=(ButtonChild)sender;
            AllocationDesk desk = new AllocationDesk(button.orderId);
            desk.ShowDialog();
        }
        private Image ChangeAlpha(Image image)
        {
            Bitmap img = new Bitmap(image);
            using (Bitmap bmp = new Bitmap(img.Width, img.Height, System.Drawing.Imaging.PixelFormat.Format32bppArgb))
            {
                using (Graphics g = Graphics.FromImage(bmp))
                {
                    g.DrawImage(img, 0, 0);
                    for (int h = 0; h <= img.Height - 1; h++)
                    {
                        for (int w = 0; w <= img.Width - 1; w++)
                        {
                            Color c = img.GetPixel(w, h);
                            bmp.SetPixel(w, h, Color.FromArgb(200, c.R, c.G, c.B));
                        }
                    }
                    return (Image)bmp.Clone();
                }
            }
        }
        public static void printRefundCp(string result, double refundMoney)
        {
            try
            {
                if (result != string.Empty)
                {
                    Console.WriteLine("print" + result);
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                        if (jsonObect["printers"] == null)
                        {
                            MessageBox.Show("未设置打印机");
                        }
                        else
                        {
                            OrderInfo orderInfo = new OrderInfo();
                            orderInfo.ID = int.Parse(jsonObect["ID"].ToString());
                            orderInfo.refundMoney = refundMoney;
                            orderInfo.orderNO = jsonObect["orderNO"].ToString();
                            if (jsonObect["payNO"] != null)
                            {
                                orderInfo.payNO = jsonObect["payNO"].ToString();
                            }
                            if (jsonObect["totalNum"] != null && int.Parse(jsonObect["totalNum"].ToString()) != 0)
                            {
                                orderInfo.peocount = jsonObect["totalNum"].ToString();
                            }
                            else
                            {
                                orderInfo.peocount = "未填";
                            }
                            orderInfo.createTime = long.Parse(jsonObect["createTime"].ToString());
                            orderInfo.payStatus = int.Parse(jsonObect["payStatus"].ToString());
                            if (jsonObect["deskName"] != null)
                            {
                                orderInfo.deskName = jsonObect["deskName"].ToString();
                            }
                            if (jsonObect["primeMoney"] != null)
                            {
                                orderInfo.primeMoney = double.Parse(jsonObect["primeMoney"].ToString());
                            }
                            if (jsonObect["saleMoney"] != null)
                            {
                                orderInfo.saleMoney = double.Parse(jsonObect["saleMoney"].ToString());
                            }

                            if (jsonObect["paidMoney"] != null)
                            {
                                orderInfo.paidMoney = double.Parse(jsonObect["paidMoney"].ToString());
                            }
                            if (jsonObect["vipMoney"] != null)
                            {
                                orderInfo.memberMoney = double.Parse(jsonObect["vipMoney"].ToString());
                            }

                            if (jsonObect["deskNO"] != null)
                            {
                                orderInfo.deskName = jsonObect["deskName"].ToString();
                                orderInfo.deskNO = jsonObect["deskNO"].ToString();
                            }
                            if (jsonObect["remark"] != null)
                            {
                                orderInfo.remark = jsonObect["remark"].ToString();
                            }
                            if (jsonObect["payType"] != null)
                            {
                                orderInfo.payType = int.Parse(jsonObect["payType"].ToString());
                            }
                            orderInfo.serviceStatus = int.Parse(jsonObect["serviceStatus"].ToString());
                            orderInfo.ordinal = int.Parse(jsonObect["ordinal"].ToString());
                            if (jsonObect["payURL"] != null)
                            {
                                orderInfo.payURL = jsonObect["payURL"].ToString();
                            }

                            if (jsonObect["payTime"] != null)
                            {
                                orderInfo.payTime = long.Parse(jsonObect["payTime"].ToString());
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

                            List<Cp> cps = new List<Cp>();
                            List<Cp> cpChilds = new List<Cp>();
                            if (jsonObect["attached"] != null)
                            {
                                JArray cpArray = JArray.Parse(jsonObect["attached"].ToString());

                                for (int i = 0; i < cpArray.Count; i++)
                                {
                                    Cp cp = new Cp();
                                    cp.id = int.Parse(cpArray[i]["ID"].ToString());
                                    cp.name = cpArray[i]["name"].ToString();
                                    cp.count = int.Parse(cpArray[i]["count"].ToString());
                                    cp.price = double.Parse(cpArray[i]["price"].ToString());
                                    cp.ODID = int.Parse(cpArray[i]["ODID"].ToString());
                                    if (cpArray[i]["delFlag"] != null)
                                    {
                                        cp.flag = int.Parse(cpArray[i]["delFlag"].ToString());
                                    }
                                    else
                                    {
                                        cp.flag = 0;
                                    }
                                    if (cpArray[i]["vipPrice"] != null)
                                    {
                                        cp.memberPrice = double.Parse(cpArray[i]["vipPrice"].ToString());
                                    }

                                    cp.printID = int.Parse(cpArray[i]["printID"].ToString());

                                    if (cpArray[i]["attrs"] != null)
                                    {
                                        JArray attrArray = JArray.Parse(cpArray[i]["attrs"].ToString());
                                        List<CpAttribution> attrs = new List<CpAttribution>();
                                        for (int j = 0; j < attrArray.Count; j++)
                                        {
                                            CpAttribution attr = new CpAttribution();
                                            attr.id = int.Parse(attrArray[j]["ID"].ToString());
                                            attr.text = attrArray[j]["text"].ToString();
                                            attrs.Add(attr);
                                        }
                                        cp.attrs = attrs;
                                    }
                                    if (cpArray[i]["subItems"] != null)
                                    {
                                        cp.subItems = cpArray[i]["subItems"].ToString();
                                        cpChilds.Add(cp);
                                    }
                                    else
                                    {
                                        if (cpArray[i]["isSub"] != null)
                                        {
                                            cp.isSub = int.Parse(cpArray[i]["isSub"].ToString());
                                        }
                                        cps.Add(cp);
                                    }


                                }


                                foreach (Cp cp in cpChilds)
                                {
                                    List<Cp> ccc = new List<Cp>();
                                    foreach (Cp cp2 in cps)
                                    {
                                        if (cp.ODID == cp2.ODID)
                                        {
                                            if (ccc.Count > 0)
                                            {
                                                if (!ccc.Exists(o => o.id == cp2.id))
                                                {
                                                    ccc.Add(cp2);
                                                }
                                            }
                                            else
                                            {
                                                ccc.Add(cp2);
                                            }
                                        }

                                    }
                                    cp.cpChild = ccc;
                                }

                                foreach (Cp cp in cps)
                                {
                                    if (cp.isSub == 0)
                                    {
                                        cpChilds.Add(cp);
                                    }

                                }

                                //打印预览            
                                PrintPreviewDialog ppd = new PrintPreviewDialog();
                                for (int i = 0; i < prints.Count; i++)
                                {
                                    List<Cp> printCps = new List<Cp>();
                                    for (int j = 0; j < cpChilds.Count; j++)
                                    {
                                        if (cpChilds[j].printID == prints[i].id)
                                        {

                                            printCps.Add(cpChilds[j]);
                                        }
                                    }
                                    PrintDocumentChild pd = new PrintDocumentChild();
                                    pd.orderInfo = orderInfo;
                                    pd.print = prints[i];
                                    pd.print.cps = printCps;
                                    pd.PrinterSettings.PrinterName = prints[i].name;

                                    //设置边距

                                    Margins margin = new Margins(20, 20, 20, 20);

                                    pd.DefaultPageSettings.Margins = margin;


                                    ////纸张设置默认

                                    PaperSize pageSize = new PaperSize("First custom size", getYc(80), int.MaxValue);

                                    pd.DefaultPageSettings.PaperSize = pageSize;



                                    //打印事件设置            

                                    pd.PrintPage += new PrintPageEventHandler(printStrRefund);

                                    ppd.Document = pd;
                                    if (prints[i].isPay != 0)
                                    {
                                        pd.Print();
                                    }

                                }

                            }
                            else
                            {
                                MessageBox.Show("服务器异常：无菜品数据");
                            }

                        }

                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                    }


                }
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        public static void printStrRefund(object sender, PrintPageEventArgs e)

        {

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
            type.Text = printDocumentChild.print.name;
            type.Left = left;
            type.Font = new Font("微软雅黑", 10);
            type.ForeColor = Color.Gray;
            type.Top = top + type.Height + shopName.Top - 3;

            Label u = new Label();
            u.Text = "----------------退单---------------------";
            u.Left = left;
            u.Font = new Font("微软雅黑", 10);
            u.ForeColor = Color.Gray;
            u.Top = top + u.Height + type.Top - 3;

            Label dNumber = new Label();
            Label odinal = new Label();
            odinal.Text = "订单序号: " + printDocumentChild.orderInfo.ordinal;
            odinal.Left = left;
            odinal.Font = new Font("微软雅黑", 10);
            odinal.ForeColor = Color.Gray;

            if (printDocumentChild.orderInfo.deskName != null)
            {
                dNumber.Text = "桌台: " + printDocumentChild.orderInfo.deskName;
                dNumber.Left = left;
                dNumber.Font = new Font("微软雅黑", 10);
                dNumber.ForeColor = Color.Gray;
                dNumber.Top = top + dNumber.Height + u.Top - 3;
                odinal.Top = top + odinal.Height + dNumber.Top - 3;
                p.Controls.Add(dNumber);
            }
            else
            {
                odinal.Top = top + odinal.Height + u.Top - 3;
            }
            Label peocount = new Label();
            peocount.Text = "人数: " + printDocumentChild.orderInfo.peocount;
            peocount.Left = left + 150;
            peocount.Font = new Font("微软雅黑", 10);
            peocount.ForeColor = Color.Gray;
            peocount.Top = top + dNumber.Height + u.Top - 3;
            p.Controls.Add(peocount);


            Label printDate = new Label();
            printDate.Text = "打印日期: " + DateTime.Now.ToShortDateString();
            printDate.Left = left;
            printDate.Font = new Font("微软雅黑", 10);
            printDate.ForeColor = Color.Gray;
            printDate.Top = top + printDate.Height + odinal.Top - 3;

            Label orderNo = new Label();
            orderNo.Text = "单号: " + printDocumentChild.orderInfo.orderNO;
            orderNo.Left = left;
            orderNo.Font = new Font("微软雅黑", 10);
            orderNo.ForeColor = Color.Gray;
            orderNo.Top = top + orderNo.Height + printDate.Top - 3;


            System.DateTime orderTIME = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
            DateTime dt = orderTIME.AddMilliseconds(printDocumentChild.orderInfo.createTime);
            Label orderDate = new Label();
            orderDate.Text = "下单时间: " + dt.ToString("yyyy/MM/dd HH:mm:ss"); 
            orderDate.Left = left;
            orderDate.Font = new Font("微软雅黑", 10);
            orderDate.ForeColor = Color.Gray;
            orderDate.Top = top + orderDate.Height + orderNo.Top - 3;

            Label u2 = new Label();
            u2.Text = "--------------------------------------------";
            u2.Left = left;
            u2.Font = new Font("微软雅黑", 10);
            u2.ForeColor = Color.Gray;
            u2.Top = top + u2.Height + orderDate.Top - 3;

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
            cpPrice.Text = "单价";
            cpPrice.Left = left + 230;
            cpPrice.Font = new Font("微软雅黑", 10);
            cpPrice.ForeColor = Color.Gray;
            cpPrice.Top = top + cpName.Height + u2.Top;
            p.Controls.Add(cpName);
            p.Controls.Add(cpCount);
            p.Controls.Add(cpPrice);
            int t = 0;
            int l = 0;
            int nameTop = 0;
            int countTop = 0;
            int priceTop = 0;
            int all = 0;
            for (int i = 0; i < printDocumentChild.print.cps.Count; i++)
            {

                if (i != 0 && printDocumentChild.print.cps[i - 1].subItems != null && printDocumentChild.print.cps[i].subItems == null)
                {
                    if (printDocumentChild.print.cps[i - 1].cpChild.Count > 2)
                    {
                        // l += 10;
                    }
                    else
                    {
                        // l += 20;
                    }

                }
                Label name = new Label();
                name.AutoSize = true;
                name.Left = left;
                name.ForeColor = Color.Gray;
                Label count = new Label();
                count.Left = left + 150;
                count.ForeColor = Color.Gray;
                Label price = new Label();
                price.Left = left + 235;
                price.ForeColor = Color.Gray;

                //  name.Top = top + name.Height + cpPrice.Top + l + i * 20;
                //  count.Top = top + count.Height + cpPrice.Top + l + i * 20;
                //  price.Top = top + price.Height + cpPrice.Top + l + i * 20;

                name.Top = top + name.Height + cpPrice.Top + l + i * 20 + nameTop;
                count.Top = top + count.Height + cpPrice.Top + l + i * 20 + countTop;
                price.Top = top + price.Height + cpPrice.Top + l + i * 20 + priceTop;

                if (printDocumentChild.print.cps[i].attrs != null)
                {
                    string attrs = "";
                    for (int j = 0; j < printDocumentChild.print.cps[i].attrs.Count; j++)
                    {
                        if (printDocumentChild.print.cps[i].attrs.Count - 1 == j)
                        {
                            attrs += printDocumentChild.print.cps[i].attrs[j].text;
                        }
                        else
                        {
                            attrs += printDocumentChild.print.cps[i].attrs[j].text + ",";
                        }

                    }

                    name.Text = printDocumentChild.print.cps[i].name + "(" + attrs + ")";
                    count.Text = printDocumentChild.print.cps[i].count.ToString();
                    price.Text = printDocumentChild.print.cps[i].price.ToString();

                }
                else
                {
                    name.Text = printDocumentChild.print.cps[i].name;
                    count.Text = printDocumentChild.print.cps[i].count.ToString();
                    price.Text = printDocumentChild.print.cps[i].price.ToString();

                }




                if (printDocumentChild.print.cps[i].subItems != null)
                {
                    for (int j = 0; j < printDocumentChild.print.cps[i].cpChild.Count; j++)
                    {
                        name.Text += "\r\n     ↳" + printDocumentChild.print.cps[i].cpChild[j].name;

                        if (printDocumentChild.print.cps[i].cpChild.Count <= 2)
                        {
                            l += 20;
                        }
                        else
                        {
                            l += 20;
                        }
                    }

                }


                if (printDocumentChild.print.cps[i].flag == -1)
                {
                    name.Text += "(退)";
                    name.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    price.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                    count.Font = new Font("微软雅黑", 10, FontStyle.Strikeout);
                }
                else
                {
                    price.Font = new Font("微软雅黑", 10);
                    name.Font = new Font("微软雅黑", 10);
                    count.Font = new Font("微软雅黑", 10);
                }

                if (printDocumentChild.print.cps[i].remark != null && printDocumentChild.print.cps[i].remark != "")
                {
                    string nn = "\r\n   (注)" + printDocumentChild.print.cps[i].remark;
                    int jd = nn.Length / 20;
                    if (nn.Length != 20 && nn.Length > 20)
                    {
                        for (int m = 1; m <= jd; m++)
                        {
                            if (m != 1)
                            {
                                nn = nn.Insert(m * 20 + 5, "\r\n   ");
                            }
                            else
                            {
                                nn = nn.Insert(m * 23, "\r\n   ");
                            }


                        }
                    }
                    else
                    {
                        jd = 0;
                    }

                    if (jd == 0)
                    {
                        nameTop += 20;
                        countTop += 20;
                        priceTop += 20;
                        all = 20;
                    }
                    else
                    if (jd == 1)
                    {
                        nameTop += 40;
                        countTop += 40;
                        priceTop += 40;
                        all = 40;
                    }
                    else
                    {
                        nameTop += jd * 20 + 20;
                        countTop += jd * 20 + 20;
                        priceTop += jd * 20 + 20;
                        all = jd * 20;
                    }
                    name.Text += nn;
                }
                else
                {
                    all = 20;
                }

                p.Controls.Add(name);
                p.Controls.Add(count);
                p.Controls.Add(price);



                if (printDocumentChild.print.cps.Count == 1)
                {
                    if (printDocumentChild.print.cps[i].remark != null && printDocumentChild.print.cps[i].remark != "")
                    {
                        t = l + price.Top + all;

                    }
                    else
                    {
                        t = l + price.Top;
                    }

                }
                else
                {
                    t = price.Top + all;
                }


            }

            Label u3 = new Label();
            u3.Text = "--------------------------------------------";
            u3.Left = left;
            u3.Font = new Font("微软雅黑", 10);
            u3.ForeColor = Color.Gray;
            u3.Top = top + u3.Height + t;
            p.Controls.Add(u3);

            Label primeMoney = new Label();
            primeMoney.Text = "合计: ￥" + printDocumentChild.orderInfo.primeMoney;
            primeMoney.Left = left;
            primeMoney.Font = new Font("微软雅黑", 10);
            primeMoney.ForeColor = Color.Gray;
            primeMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(primeMoney);

            Label memberMoney = new Label();
            memberMoney.Text = "会员价: ￥" + printDocumentChild.orderInfo.memberMoney;
            memberMoney.Left = left + 150;
            memberMoney.Font = new Font("微软雅黑", 10);
            memberMoney.ForeColor = Color.Gray;
            memberMoney.Top = top + primeMoney.Height + u3.Top;
            p.Controls.Add(memberMoney);

            Label remark = new Label();
            remark.Text = "备注: " + printDocumentChild.orderInfo.remark;
            remark.Left = left;
            remark.Font = new Font("微软雅黑", 10);
            remark.ForeColor = Color.Gray;
            remark.Top = top + remark.Height + primeMoney.Top;
            p.Controls.Add(remark);

            if (printDocumentChild.orderInfo.payStatus == 0)
            {

            }
            else if (printDocumentChild.orderInfo.payStatus == 1|| printDocumentChild.orderInfo.payNO!=null)
            {
                Label u5 = new Label();
                u5.Text = "--------------------------------------------";
                u5.Left = left;
                u5.Font = new Font("微软雅黑", 10);
                u5.ForeColor = Color.Gray;
                //u5.Top = top + u5.Height + remark.Top;
                u5.Top = top + u5.Height + remark.Top;
                p.Controls.Add(u5);

                string payType = "";
                if (printDocumentChild.orderInfo.payType == 1)
                {
                    payType = "微信";
                }
                else if (printDocumentChild.orderInfo.payType == 2)
                {
                    payType = "支付宝";
                }
                else if (printDocumentChild.orderInfo.payType == 3)
                {
                    payType = "现金";

                }
                else if (printDocumentChild.orderInfo.payType == 4)
                {
                    payType = "刷卡";
                }
                else if (printDocumentChild.orderInfo.payType == 5)
                {
                    payType = "其他";
                }
                else if (printDocumentChild.orderInfo.payType == 10)
                {
                    payType = "会员卡";
                }

                Label refund = new Label();
                refund.Text = "退款金额: ￥" + (printDocumentChild.orderInfo.refundMoney);
                refund.Left = left;
                refund.Font = new Font("微软雅黑", 10);
                refund.ForeColor = Color.Gray;
                refund.Top = top + refund.Height + u5.Top;
                p.Controls.Add(refund);

                Label payTypes = new Label();
                payTypes.Text = "支付方式: " + payType;
                payTypes.Left = left;
                payTypes.Font = new Font("微软雅黑", 10);
                payTypes.ForeColor = Color.Gray;
                payTypes.Top = top + payTypes.Height + refund.Top;
                p.Controls.Add(payTypes);

                Label y = new Label();
                if (double.Parse(printDocumentChild.orderInfo.primeMoney.ToString()) - double.Parse(printDocumentChild.orderInfo.paidMoney.ToString()) < 0)
                {
                    y.Text = "优惠: ￥0";
                }
                else
                {
                    y.Text = "优惠: ￥" + (double.Parse(printDocumentChild.orderInfo.primeMoney.ToString()) - double.Parse(printDocumentChild.orderInfo.paidMoney.ToString()));
                }
               
                y.Left = left + 150;
                y.Font = new Font("微软雅黑", 10);
                y.ForeColor = Color.Gray;
                y.Top = top + refund.Height + u5.Top;
                p.Controls.Add(y);

               

                Label payNO = new Label();
                payNO.Text = "支付号: " + printDocumentChild.orderInfo.payNO;
                payNO.Left = left;
                payNO.Font = new Font("微软雅黑", 10);
                payNO.ForeColor = Color.Gray;
                payNO.Top = top + payNO.Height + payTypes.Top;
                p.Controls.Add(payNO);

                System.DateTime payTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)); // 当地时区
                DateTime dt1 = payTime.AddMilliseconds(printDocumentChild.orderInfo.payTime);

                Label payTimes = new Label();
                payTimes.Text = "支付时间: " + dt1.ToString("yyyy/MM/dd HH:mm:ss");
                payTimes.Left = left;
                payTimes.Font = new Font("微软雅黑", 10);
                payTimes.ForeColor = Color.Gray;
                payTimes.Top = top + payTimes.Height + payNO.Top;
                p.Controls.Add(payTimes);

               

            }
            p.Controls.Add(shopName);
            p.Controls.Add(type);
            p.Controls.Add(u);
            p.Controls.Add(dNumber);
            p.Controls.Add(odinal);
            p.Controls.Add(printDate);
            p.Controls.Add(orderNo);
            p.Controls.Add(orderDate);
            p.Controls.Add(u2);


            foreach (Control c in p.Controls)
            {
                if (c.GetType() == typeof(Label))
                {
                    e.Graphics.DrawString(c.Text, c.Font, new SolidBrush(c.ForeColor), c.Location);
                }

            }

        }

        private void MainForm_SizeChanged(object sender, EventArgs e)
        {
        
        }
    }

    }
