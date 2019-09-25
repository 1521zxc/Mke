using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

namespace DeviceDecode
{
    public partial class desk : UserControl
    {
        public List<DeskInfo> deskList;
        public string deskName = "";
        public  int deskState=-1;
        public int orderId=0;
        public desk()
        {
            InitializeComponent();
            SetSearchDefaultText();
            this.desk_right_top.Visible = false;
            this.desk_right_bottom.Visible = false;
        }

        private void button7_Click(object sender, EventArgs e)
        {
            try
            {
                if (this.eatPeoCount.Text.Equals("未填人数"))
                {
                    this.eatPeoCount.Text = "1";
                }
                else
                {
                    this.eatPeoCount.Text = (int.Parse(this.eatPeoCount.Text.ToString()) + 1).ToString();
                }
               
            } catch (Exception q) { MessageBox.Show("人数类型错误"); }
               
        }

        private void button8_Click(object sender, EventArgs e)
        {
            try
            {
                if (this.eatPeoCount.Text.Equals("未填人数"))
                {
                    this.eatPeoCount.Text = "1";
                }
                else
                if (int.Parse(this.eatPeoCount.Text.ToString()) <= 1)
                {
                    MessageBox.Show("人数已达最小数啦");
                }
                else
                {
                    this.eatPeoCount.Text = (int.Parse(this.eatPeoCount.Text.ToString()) - 1).ToString();

                }
               
            }
            catch (Exception q) { MessageBox.Show("人数类型错误"); }

        }

      

        private void button2_Click(object sender, EventArgs e)
        {
            SetSearchDefaultText();
            this.search.BackgroundImage = Properties.Resources.nullButtonState;
            this.button3.BackgroundImage = Properties.Resources.eatButtonState;
            //this.button2.BackgroundImage = Properties.Resources.yd_click;
            deskState = 2;

            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

            JObject json3 = null;

            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 12 }, { "deskStatus", deskState } };

            json2.Add("data", json3.ToString());
            Console.WriteLine(json2.ToString());
            string result = string.Empty;
            result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
            Console.WriteLine(result);
            MainForm.getDesks(result, true);
        }

        private void button5_Click(object sender, EventArgs e)
        {
            if (pageIndex.Text == "0")
            {
                MessageBox.Show("暂无记录");
            }
            else
            {
                if (this.page.Text == this.pageIndex.Text)
                {
                    MessageBox.Show("已经到最后一页啦");
                }
                else
                {
                    this.pageIndex.Text = (int.Parse(this.pageIndex.Text) + 1).ToString();
                    JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

                    JObject json3 = null;
                    if (deskState != -1)
                    {
                        json3 = new JObject { { "totalCount", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "pageSize", 12 }, { "deskStatus", deskState } };
                    }
                    else
                    {
                        json3 = new JObject { { "totalCount", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "pageSize", 12 } };
                    }

                    json2.Add("data", json3.ToString());
                    string result = string.Empty;
                    result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
                    Console.WriteLine(result);
                    MainForm.getDesks(result, false);
                }
            }
            
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (pageIndex.Text == "0")
            {
                MessageBox.Show("暂无记录");
            }
            else
            {
                if ("1" == this.pageIndex.Text)
                {
                    MessageBox.Show("已经到首页啦");
                }
                else
                {

                    this.pageIndex.Text = (int.Parse(this.pageIndex.Text) - 1).ToString();
                    JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

                    JObject json3 = null;
                    if (deskState != -1)
                    {
                        json3 = new JObject { { "totalCount", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "pageSize", 12 }, { "deskStatus", deskState } };
                    }
                    else
                    {
                        json3 = new JObject { { "totalCount", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "pageSize", 12 } };
                    }

                    json2.Add("data", json3.ToString());
                    string result = string.Empty;
                    result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
                    Console.WriteLine(result);
                    MainForm.getDesks(result, false);
                }
            }
           
        }

        private void search_Click(object sender, EventArgs e)
        {
            SetSearchDefaultText();
            this.search.BackgroundImage = Properties.Resources.nullButtonState_click;
            this.button3.BackgroundImage = Properties.Resources.eatButtonState;
           // this.button2.BackgroundImage = Properties.Resources.ydButtonState;
            deskState = 0;
          
            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

            JObject json3 = null;
     
            json3 = new JObject { { "totalCount", 1 },  { "pageSize", 12 }, { "deskStatus", deskState } };

            json2.Add("data", json3.ToString());
            string result = string.Empty;
            result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
            Console.WriteLine(result);
            MainForm.getDesks(result,true);
        }

        private void button3_Click(object sender, EventArgs e)
        {
            SetSearchDefaultText();
            this.search.BackgroundImage = Properties.Resources.nullButtonState;
            this.button3.BackgroundImage = Properties.Resources.eatButtonState_click;
        //    this.button2.BackgroundImage = Properties.Resources.ydButtonState;
            deskState = 1;

            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

            JObject json3 = null;

            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 12 }, { "deskStatus", deskState } };

            json2.Add("data", json3.ToString());
            Console.WriteLine(json2.ToString());
            string result = string.Empty;
            result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
            Console.WriteLine(result);
            MainForm.getDesks(result,true);
        }

        private void button6_Click(object sender, EventArgs e)
        {
            string str = "";
            if (MainForm.sellState == 0)
            {
                str = "未支付";

            }
            else if (MainForm.sellState == 5)
            {
                str = "支付失败";
            }
            else if (MainForm.sellState == 6)
            {
                str = "支付中";

            }
            else if (MainForm.sellState == 7)
            {
                str = "退款中";

            }
            else if (MainForm.sellState == 8)
            {
                str = "退款失败";

            }
            else if (MainForm.sellState == 9)
            {
                str = "已退款";

            }
            else if (MainForm.sellState == 10)
            {
                str = "订单已改变";
            }
            else 
            {
                str = "已付款";
            }
            if (MainForm.orderNo != null)
            {
                if (MessageBox.Show("此桌台的订单" + str + "，确认清理吗？", "确认", MessageBoxButtons.OKCancel, MessageBoxIcon.Question, MessageBoxDefaultButton.Button2) == DialogResult.OK)
                {
                    JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 49 }, { "token", Login.userInfo.token } };

                    JObject json3 = null;
                    List<string> os = new List<string>();
                    os.Add(MainForm.orderNo);
                    string jsonData = JsonConvert.SerializeObject(os);
                    JArray attached = JArray.Parse(jsonData);

                    json3 = new JObject { { "orderNOs", attached } };

                    json2.Add("data", json3.ToString());
                    Console.WriteLine(json2.ToString());
                    string result = string.Empty;
                    result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
                    if (result != string.Empty)
                    {
                        Console.WriteLine("clearDesk" + result);
                        JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                        if (jo["errorCode"].ToString().Equals("0"))
                        {
                            MessageBox.Show("桌台清理成功");
                            foreach (DeskInfo desk in deskList)
                            {
                                Console.WriteLine(desk.deskNO+"|"+ desk.deskName);
                                if (desk.orderNO!=null)
                                {
                                    if (desk.orderNO.Equals(MainForm.orderNo))
                                    {
                                        desk.deskStatus = "0";
                                        desk.orderNO = null;
                                    }
                                }
                            }
                            getLocalDesk(deskList);
                        }
                        else
                        {
                            MessageBox.Show(jo["message"].ToString());
                        }
                    }
                }
            }
            else
            {
                MessageBox.Show("服务器异常：无单号");
            }           
            
        }

        private void button9_Click(object sender, EventArgs e)
        {
            if (orderId!=0)
            {
                try
                {
                    if (int.Parse(this.eatPeoCount.Text.ToString()) < 0)
                    {
                        MessageBox.Show("人数类型错误"); return;
                    }
                    else
                    {
                        this.eatPeoCount.Text = (int.Parse(this.eatPeoCount.Text.ToString())).ToString();
                    }
                    
                }
                catch (Exception q) { MessageBox.Show("人数类型错误"); return; }

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 92 }, { "muuid", Login.userInfo.muuid } };

                JObject json3 = null;

                json3 = new JObject { { "totalNum", int.Parse(this.eatPeoCount.Text) }, { "ID", orderId } };


                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
                if (result != string.Empty)
                {
                    Console.WriteLine("eatCount" + result);
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        MessageBox.Show("修改成功");
                        foreach (DeskInfo desk in deskList)
                        {
                            //Console.WriteLine(desk.deskNO + "|" + desk.deskName);
                            if (desk.orderNO != null)
                            {
                                if (desk.orderNO.Equals(MainForm.orderNo))
                                {
                                   
                                    desk.totalNum = int.Parse(this.eatPeoCount.Text);
                                }
                            }
                        }
                        getLocalDesk(deskList);
                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                    }
                }
            }
            else
            {
                MessageBox.Show("服务器异常：无订单id");
            }

        }
        public void getLocalDesk(List<DeskInfo> deskList)
        {
            panel1.Controls.Clear();
            desk_right_center.Controls.Clear();
            panel5.Visible = false;
            desk_right_top.Visible = false;
            desk_right_bottom.Visible = false;

            MainForm.orderNo = null;

            PictureBox picture = new PictureBox();
            picture.Size = new Size(229, 172);
            picture.Location = new Point(37, 84);
            picture.BackgroundImage = Properties.Resources.noDeatail;
            desk_right_center.Controls.Add(picture);

            int j = 0;
            int k = 0;
            for (int i = 0; i < deskList.Count; i++)
            {              
                Label l1 = new Label();
                l1.BackColor = Color.Transparent;
                l1.Top = 122;
                l1.Text = deskList[i].deskNO + "|" + deskList[i].deskName;
                l1.ForeColor = Color.FromArgb(255, 102, 102, 102);

                //   CheckBoxChild ck = new CheckBoxChild();
                //   ck.Location = new Point(10, 10);
                //   ck.Visible = false;
                //   ck.BackColor = Color.Transparent;

                if (i <= 3)
                {
                    PanelChild p = new PanelChild();
                    p.deskState = int.Parse(deskList[i].deskStatus);
                    if (int.Parse(deskList[i].deskStatus) == 0)
                    {
                        p.BackgroundImage = Properties.Resources.nullState;
                    }
                    else if(int.Parse(deskList[i].deskStatus) == 1)
                    {
                        Label l2 = new Label();
                        if (deskList[i].payStatus == 1)
                        {
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
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
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == 0)
                            {
                                p.BackgroundImage = Properties.Resources.eatState;
                            }
                            else if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
                            {
                                p.BackgroundImage = Properties.Resources.eat_noservice;
                            }
                            l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                        }

                        
                        l2.AutoSize = true;
                        l2.Font = new Font("微软雅黑", 14);
                        l2.BackColor = Color.Transparent;
                        l2.Location = new Point(93, 18);
                        if (deskList[i].orderNO != null)
                        {
                            l2.Text = deskList[i].totalNum.ToString();
                            //ck.orderNo= jarry[i]["orderNO"].ToString();
                            p.orderNO = deskList[i].orderNO;
                            p.ID = deskList[i].orderID;
                        }
                      
                        p.Controls.Add(l2);
                        Label price = new Label();
                        price.BackColor = Color.Transparent;
                        price.Top = 80;
                        price.Left = 28;
                        price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;

                        price.Text = "￥" + deskList[i].saleMoney.ToString();
                            price.ForeColor = Color.White;
                        p.Controls.Add(price);

                    }
                    else if (int.Parse(deskList[i].deskStatus) == 2)
                    {
                        Label l2 = new Label();
                        if (deskList[i].payStatus == 1)
                        {
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
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
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == 0)
                            {
                                p.BackgroundImage = Properties.Resources.eatreserve;
                            }
                            else if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
                            {
                                p.BackgroundImage = Properties.Resources.reserve_noserve;
                            }
                            l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                        }

                       
                        l2.AutoSize = true;
                        l2.Font = new Font("微软雅黑", 14);
                        l2.BackColor = Color.Transparent;
                        l2.Location = new Point(93, 18);
                        if (deskList[i].orderNO != null )
                        {
                            //ck.orderNo = jarry[i]["orderNO"].ToString();
                            l2.Text = deskList[i].totalNum.ToString();
                            p.orderNO = deskList[i].orderNO.ToString();
                            p.ID = int.Parse(deskList[i].orderID.ToString());
                        }
                       
                        p.Controls.Add(l2);

                        Label price = new Label();
                        price.BackColor = Color.Transparent;
                        price.Top = 80;
                        price.Left = 28;
                        price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;


                        price.Text = "￥" + deskList[i].saleMoney.ToString();
                            price.ForeColor = Color.White;
                            p.Controls.Add(price);
                        

                    }

                    p.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
                    p.Size = new Size(163, 140);
                    p.BackColor = Color.Transparent;
                    p.Location = new Point(1 + i * 163, 0);
                    panel1.Controls.Add(p);
                    //     p.ck = ck;
                    //     p.MouseHover += new EventHandler(showStEdit);
                    p.Click += new EventHandler(MainForm.showDeskDeatail);
                    l1.Left = (p.Width / 2) - (l1.Width / 2);
                    l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                    p.Controls.Add(l1);
                }
                else if (i > 3 && i < 8)
                {
                    PanelChild p = new PanelChild();
                    p.deskState = int.Parse(deskList[i].deskStatus);
                    if (int.Parse(deskList[i].deskStatus) == 0)
                    {
                        p.BackgroundImage = Properties.Resources.nullState;
                    }
                    else if (int.Parse(deskList[i].deskStatus) == 1)
                    {
                        Label l2 = new Label();
                        if (deskList[i].payStatus == 1)
                        {
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
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
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == 0)
                            {
                                p.BackgroundImage = Properties.Resources.eatState;
                            }
                            else if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
                            {
                                p.BackgroundImage = Properties.Resources.eat_noservice;
                            }
                            l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                        }

                      
                        l2.AutoSize = true;
                        l2.Font = new Font("微软雅黑", 14);
                        l2.BackColor = Color.Transparent;
                        l2.Location = new Point(93, 18);
                        if (deskList[i].orderNO!= null)
                        {
                            //             ck.orderNo = jarry[i]["orderNO"].ToString();
                            l2.Text = deskList[i].totalNum.ToString();
                            p.orderNO = deskList[i].orderNO.ToString();
                            p.ID = deskList[i].orderID;
                        }
                        
                        p.Controls.Add(l2);
                        Label price = new Label();
                        price.BackColor = Color.Transparent;
                        price.Top = 80;
                        price.Left = 28;
                        price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;

                        price.Text = "￥" + deskList[i].saleMoney.ToString();
                            price.ForeColor = Color.White;
                        p.Controls.Add(price);
                    

                    }
                    else if (int.Parse(deskList[i].deskStatus) == 2)
                    {
                        Label l2 = new Label();
                        if (deskList[i].payStatus == 1)
                        {
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
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
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == 0)
                            {
                                p.BackgroundImage = Properties.Resources.eatreserve;
                            }
                            else if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
                            {
                                p.BackgroundImage = Properties.Resources.reserve_noserve;
                            }
                            l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                        }
                        l2.AutoSize = true;
                        l2.Font = new Font("微软雅黑", 14);
                        l2.BackColor = Color.Transparent;
                        l2.Location = new Point(93, 18);
                        if (deskList[i].orderNO != null)
                        {
                            //ck.orderNo = jarry[i]["orderNO"].ToString();
                            l2.Text = deskList[i].totalNum.ToString();
                            p.orderNO = deskList[i].orderNO.ToString();
                            p.ID = int.Parse(deskList[i].orderID.ToString());
                        }
                       
                        p.Controls.Add(l2);

                        Label price = new Label();
                        price.BackColor = Color.Transparent;
                        price.Top = 80;
                        price.Left = 28;
                        price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;


                        price.Text = "￥" + deskList[i].saleMoney.ToString();
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
                    panel1.Controls.Add(p);
                    p.Click += new EventHandler(MainForm.showDeskDeatail);
                    l1.Left = (p.Width / 2) - (l1.Width / 2);
                    l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                    p.Controls.Add(l1);
                }
                else if (i >= 8 && i < 12)
                {
                    PanelChild p = new PanelChild();
                    p.deskState = int.Parse(deskList[i].deskStatus);
                    if (int.Parse(deskList[i].deskStatus) == 0)
                    {
                        p.BackgroundImage = Properties.Resources.nullState;
                    }
                    else if (int.Parse(deskList[i].deskStatus) == 1)
                    {
                        Label l2 = new Label();
                        if (deskList[i].payStatus == 1)
                        {
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
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
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == 0)
                            {
                                p.BackgroundImage = Properties.Resources.eatState;
                            }
                            else if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
                            {
                                p.BackgroundImage = Properties.Resources.eat_noservice;
                            }

                            l2.ForeColor = Color.FromArgb(255, 245, 99, 132);
                        }
                        l2.AutoSize = true;
                        l2.Font = new Font("微软雅黑", 14);
                        l2.BackColor = Color.Transparent;
                        l2.Location = new Point(93, 18);

                        if (deskList[i].orderNO!= null)
                        {
                            //        ck.orderNo = jarry[i]["orderNO"].ToString();
                            l2.Text = deskList[i].totalNum.ToString();
                            p.orderNO = deskList[i].orderNO;
                            p.ID = deskList[i].orderID;
                        }
                        p.Controls.Add(l2);
                        Label price = new Label();
                        price.BackColor = Color.Transparent;
                        price.Top = 80;
                        price.Left = 28;
                        price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;

                        price.Text = "￥" + deskList[i].saleMoney.ToString();
                            price.ForeColor = Color.White;
                        p.Controls.Add(price);
                      
                    }
                    else if (int.Parse(deskList[i].deskStatus) == 2)
                    {
                        Label l2 = new Label();
                        if (deskList[i].payStatus == 1)
                        {
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
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
                            if (int.Parse(deskList[i].serviceStatus.ToString()) == 0)
                            {
                                p.BackgroundImage = Properties.Resources.eatreserve;
                            }
                            else if (int.Parse(deskList[i].serviceStatus.ToString()) == -1)
                            {
                                p.BackgroundImage = Properties.Resources.reserve_noserve;
                            }

                            l2.ForeColor = Color.FromArgb(255, 235, 161, 18);
                        }
                        l2.AutoSize = true;
                        l2.Font = new Font("微软雅黑", 14);
                        l2.BackColor = Color.Transparent;
                        l2.Location = new Point(93, 18);
                        if (deskList[i].orderNO != null)
                        {
                            //ck.orderNo = jarry[i]["orderNO"].ToString();
                            l2.Text = deskList[i].totalNum.ToString();
                            p.orderNO = deskList[i].orderNO.ToString();
                            p.ID = int.Parse(deskList[i].orderID.ToString());
                        }
                       
                        p.Controls.Add(l2);

                        Label price = new Label();
                        price.BackColor = Color.Transparent;
                        price.Top = 80;
                        price.Left = 28;
                        price.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                        price.Text = "￥" + deskList[i].saleMoney.ToString();
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
                    panel1.Controls.Add(p);
                    p.Click += new EventHandler(MainForm.showDeskDeatail);
                    l1.Left = (p.Width / 2) - (l1.Width / 2);
                    l1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                    p.Controls.Add(l1);
                }
                //d.Show();
                //this.panelAll.Controls.Add(d);
            }
        }

        private void button16_Click(object sender, EventArgs e)
        {
            if (MainForm.orderNo == null)
            {
                MessageBox.Show("请选择一个订单");
            }
            else
            {
                orderOperation op = new orderOperation();
                op.ShowDialog();
            }
        }

        private void button15_Click(object sender, EventArgs e)
        {
            if (MainForm.orderNo == null)
            {
                MessageBox.Show("请选择一个订单");
            }
            else
            {
                orderOperation op = new orderOperation();
                op.label5.Text = "换菜";
                op.ShowDialog();
            }
        }

        private void button14_Click(object sender, EventArgs e)
        {
            if (MainForm.orderNo == null)
            {
                MessageBox.Show("请选择一个订单");
            }
            else
            {
                orderOperation op = new orderOperation();
                op.label5.Text = "退菜";
                op.ShowDialog();
            }
        }

        private void button13_Click(object sender, EventArgs e)
        {
            if (MainForm.orderNo == null)
            {
                MessageBox.Show("请选择一个订单");
            }
            else
            {
                ChangeDesk changeDesk = new ChangeDesk(orderId, deskName);
                changeDesk.ShowDialog();
            }
        }

        private void eatPeoCount_Enter(object sender, EventArgs e)
        {
            if (this.eatPeoCount.Text.Equals("未填人数"))
            {
                this.eatPeoCount.Text = "1";
            }
        }

        private void button4_Click(object sender, EventArgs e)
        {
            SetSearchDefaultText();
            this.button4.BackColor = Color.Transparent;
            this.button4.Text = "· 桌台";
            this.search.BackgroundImage = Properties.Resources.nullButtonState;
            this.button3.BackgroundImage = Properties.Resources.eatButtonState;
           // this.button2.BackgroundImage = Properties.Resources.ydButtonState;
            deskState = -1;

            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

            JObject json3 = null;

            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 12 } };

            json2.Add("data", json3.ToString());
            Console.WriteLine(json2.ToString());
            string result = string.Empty;
            result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
            Console.WriteLine(result);
            MainForm.getDesks(result, true);
        }

        private void button10_Click(object sender, EventArgs e)
        {
            this.search.BackgroundImage = Properties.Resources.nullButtonState_click;
            this.button3.BackgroundImage = Properties.Resources.eatButtonState;
          //  this.button2.BackgroundImage = Properties.Resources.ydButtonState;
            deskState = 0;

            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

            JObject json3 = null;

            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 12 }, { "deskStatus", deskState } };

            json2.Add("data", json3.ToString());
            string result = string.Empty;
            result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
            Console.WriteLine(result);
            MainForm.getDesks(result, true);
        }

     
        public void SetSearchDefaultText()
        {
            textBox1.Text = "桌台";
            textBox1.Font = new Font("微软雅黑", 14);
            textBox1.ForeColor = Color.Gray;
        }

        private void textBox1_Enter(object sender, EventArgs e)
        {
            if (textBox1.Text == "桌台")
            {
                textBox1.Text = "";
                textBox1.Font = new Font("微软雅黑", 14);
                textBox1.ForeColor = Color.Black;
            }
        }

        

        private void button10_Click_1(object sender, EventArgs e)
        {
            if (this.textBox1.Text == "" || this.textBox1.Text == "桌台")
            {
                MessageBox.Show("请输入内容");
            }
            else
            {
                this.search.BackgroundImage = Properties.Resources.nullButtonState;
                this.button3.BackgroundImage = Properties.Resources.eatButtonState;
               // this.button2.BackgroundImage = Properties.Resources.ydButtonState;
                deskState = -1;

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

                JObject json3 = null;

                json3 = new JObject { { "totalCount", 1 }, { "pageSize", 12 }, { "search", this.textBox1.Text } };

                json2.Add("data", json3.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl, json2.ToString());
                Console.WriteLine(result);
                MainForm.getDesks(result, true);
            }
        }

        private void desk_right_top_Paint(object sender, PaintEventArgs e)
        {

        }

        private void eatPeoCount_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)//如果输入的是回车键  
            {
                this.button9_Click(sender, e);//触发button事件  
            }
        }

        
    }
}
