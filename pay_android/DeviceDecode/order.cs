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
using System.Net;
using System.IO;

namespace DeviceDecode
{
    public partial class order : UserControl
    {
        public int type = -1;
        public order()
        {
            InitializeComponent();

            this.detailPanel.AutoScroll = true;
            //this.detailPanel.HorizontalScroll.Visible = true;
            SetSearchDefaultText();
        }
        public void SetSearchDefaultText()
        {
            textBox4.Text = "桌号/订单号";
            textBox4.Font = new Font("微软雅黑", 14);
            textBox4.ForeColor = Color.Gray;
        }
        private void button4_Click(object sender, EventArgs e)
        {
            if (pageIndex.Text == "0"||int.Parse(pageIndex.Text)>int.Parse(page.Text))
            {
                MessageBox.Show("暂无记录");
            }
            else
            {
                if (type==-1)
                {
                    if (this.page.Text == this.pageIndex.Text)
                    {
                        MessageBox.Show("已经到最后一页啦");
                    }
                    else
                    {
                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        this.pageIndex.Text = (int.Parse(this.pageIndex.Text) + 1).ToString();
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 90 }, { "token", Login.userInfo.token } };

                        JObject json3 = null;
                        if (!this.textBox4.Text.Equals("桌号/订单号") && !this.textBox4.Text.Equals(""))
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "search", this.textBox4.Text } };
                        }
                        else
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 } };
                        }
                        json2.Add("data", json3.ToString());
                        Console.WriteLine(json2.ToString());

                        MainForm.getOrders(json2.ToString());
                    }
                }
                else if (type == 0)
                {
                    if (this.page.Text == this.pageIndex.Text)
                    {
                        MessageBox.Show("已经到最后一页啦");
                    }
                    else
                    {
                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        this.pageIndex.Text = (int.Parse(this.pageIndex.Text) + 1).ToString();
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 97 }, { "token", Login.userInfo.token } };
                        JObject json3 = null;
                        if (!this.textBox4.Text.Equals("姓名/手机号") && !this.textBox4.Text.Equals(""))
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 0 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "search", this.textBox4.Text } };
                        }
                        else
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 0 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 } };
                        }
                        json2.Add("data", json3.ToString());
                        Console.WriteLine(json2.ToString());
                        MainForm.getReserves(json2.ToString(),type);
                    }
                    
                }

                else if (type == 1)
                {
                    if (this.page.Text == this.pageIndex.Text)
                    {
                        MessageBox.Show("已经到最后一页啦");
                    }
                    else
                    {
                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        this.pageIndex.Text = (int.Parse(this.pageIndex.Text) + 1).ToString();
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 97 }, { "token", Login.userInfo.token } };
                        JObject json3 = null;
                        if (!this.textBox4.Text.Equals("姓名/手机号") && !this.textBox4.Text.Equals(""))
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "search", this.textBox4.Text } };
                        }
                        else
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 } };
                        }
                        json2.Add("data", json3.ToString());
                        Console.WriteLine(json2.ToString());
                        MainForm.getReserves(json2.ToString(), type);
                    }
                   
                }
               
            }
           
        }
      

        private void button3_Click(object sender, EventArgs e)
        {
           
            if (pageIndex.Text == "0" || int.Parse(pageIndex.Text) > int.Parse(page.Text))
            {
                MessageBox.Show("暂无记录");
            }
            else
            {
                if (type == -1)
                {
                    if ("1" == this.pageIndex.Text)
                    {
                        MessageBox.Show("已经是首页啦");
                    }
                    else
                    {
                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        this.pageIndex.Text = (int.Parse(this.pageIndex.Text) - 1).ToString();
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 90 }, { "token", Login.userInfo.token } };
                        JObject json3 = null;
                        if (!this.textBox4.Text.Equals("桌号/订单号") && !this.textBox4.Text.Equals(""))
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "search", this.textBox4.Text } };
                        }
                        else
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 } };
                        }

                        json2.Add("data", json3.ToString());

                        MainForm.getOrders(json2.ToString());
                    }
                }
                else if (type==0)
                {
                    if ("1" == this.pageIndex.Text)
                    {
                        MessageBox.Show("已经是首页啦");
                    }
                    else
                    {
                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        this.pageIndex.Text = (int.Parse(this.pageIndex.Text) - 1).ToString();
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 97 }, { "token", Login.userInfo.token } };
                        JObject json3 = null;
                        if (!this.textBox4.Text.Equals("桌号/订单号") && !this.textBox4.Text.Equals(""))
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 0 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "search", this.textBox4.Text } };
                        }
                        else
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 0 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 } };
                        }
                        json2.Add("data", json3.ToString());
                        Console.WriteLine(json2.ToString());
                        MainForm.getReserves(json2.ToString(), type);
                    }
                    
                }
                else if (type == 1)
                {
                    if ("1" == this.pageIndex.Text)
                    {
                        MessageBox.Show("已经是首页啦");
                    }
                    else
                    {
                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        this.pageIndex.Text = (int.Parse(this.pageIndex.Text) - 1).ToString();
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 97 }, { "token", Login.userInfo.token } };
                        JObject json3 = null;
                        if (!this.textBox4.Text.Equals("桌号/订单号") && !this.textBox4.Text.Equals(""))
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "search", this.textBox4.Text } };
                        }
                        else
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 } };
                        }
                        json2.Add("data", json3.ToString());
                        Console.WriteLine(json2.ToString());
                        MainForm.getReserves(json2.ToString(), type);
                    }
                    
                }

            }
            
        }

        private void addCp_Click(object sender, EventArgs e)
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

        private void button2_Click(object sender, EventArgs e)
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

        private void button5_Click(object sender, EventArgs e)
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

        private void button6_Click(object sender, EventArgs e)
        {
            if (MainForm.orderNo == null)
            {
                MessageBox.Show("请选择一个订单");
            }
        }
       
     

        private void button7_Click(object sender, EventArgs e)
        {
            try
            {
                if (MainForm.orderNo == null)
                {
                    MessageBox.Show("请选择一个订单");
                }
                else
                {
                    if (MainForm.sellState == 0)
                    {
                        MessageBox.Show("该订单还未支付");
                    } else if (MainForm.sellState == 9)
                    {
                        MessageBox.Show("该订单已经退过款啦");
                    }
                    else if (MainForm.payNo != null)
                    {
                        string url = "http://pay.jqepay.com/refund/atonce";

                        string result = string.Empty;
                        string param = string.Format("orderNO={0}&muuid={1}&refundAmt={2}", MainForm.orderNo, Login.userInfo.muuid, MainForm.payPrice);
                        Console.WriteLine(param);
                        result = MainForm.HttpPostData(url, param);
                        if (result != string.Empty)
                        {
                            JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                            if (jo["errorCode"].ToString().Equals("0"))
                            {
                                MessageBox.Show("退款成功");
                            }
                            else
                            {
                                MessageBox.Show(jo["message"].ToString());
                            }
                        }
                    }
                    else
                    {
                        MessageBox.Show("该订单无流水");
                    }
                }
            } catch (Exception q) { MessageBox.Show(q.Message); }
           
        }

        private void textBox4_Enter(object sender, EventArgs e)
        {
            if (textBox4.Text == "桌号/订单号")
            {
                textBox4.Text = "";
                textBox4.ForeColor = Color.Black;

            } else if (textBox4.Text == "姓名/手机号")
            {
                textBox4.Text = "";
                textBox4.ForeColor = Color.Black;
            }
        }

        private void search_Click(object sender, EventArgs e)
        {
            try
            {
                if (type == -1)
                {
                    if (!this.textBox4.Text.Equals("桌号/订单号") && !this.textBox4.Text.Equals(""))
                    {
                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 90 }, { "token", Login.userInfo.token } };
                        JObject json3 = new JObject { { "search", this.textBox4.Text }, { "totalCount", 1 }, { "pageSize", 9 } };

                        json2.Add("data", json3.ToString());

                        MainForm.getOrders(json2.ToString());
                        pageIndex.Text = "1";
                    }
                    else
                    {
                        MessageBox.Show("请输入搜索内容");
                    }
                } else if (type==0)
                {
                    if (!this.textBox4.Text.Equals("姓名/手机号") && !this.textBox4.Text.Equals(""))
                    {

                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 97 }, { "token", Login.userInfo.token } };
                        JObject json3 = new JObject { { "search", this.textBox4.Text }, { "totalCount", 1 }, { "reserveType", 0 }, { "pageSize", 9 } };

                        json2.Add("data", json3.ToString());

                        MainForm.getReserves(json2.ToString(), type);
                        pageIndex.Text = "1";
                    }
                    else
                    {
                        MessageBox.Show("请输入搜索内容");
                    }
                }
                else if (type == 1)
                {
                    if (!this.textBox4.Text.Equals("姓名/手机号") && !this.textBox4.Text.Equals(""))
                    {

                        detailPanel.Controls.Clear();
                        MainForm.orderNo = null;
                        PictureBox picture = new PictureBox();
                        picture.Size = new Size(183, 305);
                        picture.Location = new Point(73, 84);
                        picture.BackgroundImage = Properties.Resources.noDeatail;
                        detailPanel.Controls.Add(picture);
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 97 }, { "token", Login.userInfo.token } };
                        JObject json3 = new JObject { { "search", this.textBox4.Text }, { "totalCount", 1 }, { "reserveType", 1 }, { "pageSize", 9 } };

                        json2.Add("data", json3.ToString());

                        MainForm.getReserves(json2.ToString(), type);
                        pageIndex.Text = "1";
                    }
                    else
                    {
                        MessageBox.Show("请输入搜索内容");
                    }
                }

            }
            catch (Exception q)
            { MessageBox.Show(q.Message); }
        }

        private void textBox4_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)//如果输入的是回车键  
            {
                this.search_Click(sender, e);//触发button事件  
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            type = -1;
            textBox4.Text = "桌号/订单号";
            textBox4.Font = new Font("微软雅黑", 14);
            textBox4.ForeColor = Color.Gray;
            this.button1.BackColor = Color.FromArgb(255, 221, 221, 221);
            this.button1.Text = "· 订单";
            this.button2.BackColor = Color.Transparent;
            this.button2.Text = "预约";
            this.button5.BackColor = Color.Transparent;
            this.button5.Text = "预定";
            try
            {
              
                    detailPanel.Controls.Clear();
                    MainForm.orderNo = null;
                    PictureBox picture = new PictureBox();
                    picture.Size = new Size(183, 305);
                    picture.Location = new Point(73, 84);
                    picture.BackgroundImage = Properties.Resources.noDeatail;
                    detailPanel.Controls.Add(picture);
                    JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 90 }, { "token", Login.userInfo.token } };
                    JObject json3 = new JObject {  { "totalCount", 1 }, { "pageSize", 9 } };

                    json2.Add("data", json3.ToString());

                    MainForm.getOrders(json2.ToString());
                    pageIndex.Text = "1";

                
            }
            catch (Exception q)
            { MessageBox.Show(q.Message); }
        }

        private void button2_Click_1(object sender, EventArgs e)
        {
            type = 0;
            textBox4.Text = "姓名/手机号";
            textBox4.Font = new Font("微软雅黑", 14);
            textBox4.ForeColor = Color.Gray;
            this.button1.BackColor = Color.Transparent;
            this.button1.Text = "订单";
            this.button2.BackColor = Color.FromArgb(255, 221, 221, 221);
            this.button2.Text = "· 预约";
            this.button5.BackColor = Color.Transparent;
            this.button5.Text = "预定";
            try
            {
                detailPanel.Controls.Clear();
                MainForm.orderNo = null;
                PictureBox picture = new PictureBox();
                picture.Size = new Size(183, 305);
                picture.Location = new Point(73, 84);
                picture.BackgroundImage = Properties.Resources.noDeatail;
                detailPanel.Controls.Add(picture);
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 97 }, { "token", Login.userInfo.token } };
                JObject json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 0 } };

                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                MainForm.getReserves(json2.ToString(),type);
                pageIndex.Text = "1";

            }
            catch (Exception q)
            { MessageBox.Show(q.Message); }
        }

        private void button5_Click_1(object sender, EventArgs e)
        {
            type =1;
            textBox4.Text = "姓名/手机号";
            textBox4.Font = new Font("微软雅黑", 14);
            textBox4.ForeColor = Color.Gray;
            this.button1.BackColor = Color.Transparent;
            this.button1.Text = "订单";
            this.button2.BackColor = Color.Transparent;
            this.button2.Text = "预约";
            this.button5.BackColor = Color.FromArgb(255, 221, 221, 221);
            this.button5.Text = "· 预定";
            try
            {
                detailPanel.Controls.Clear();
                MainForm.orderNo = null;
                PictureBox picture = new PictureBox();
                picture.Size = new Size(183, 305);
                picture.Location = new Point(73, 84);
                picture.BackgroundImage = Properties.Resources.noDeatail;
                detailPanel.Controls.Add(picture);
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 97 }, { "token", Login.userInfo.token } };
                JObject json3 = new JObject { { "totalCount", 1 }, { "pageSize", 9 }, { "reserveType", 1 } };

                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                MainForm.getReserves(json2.ToString(), type);
                pageIndex.Text = "1";

            }
            catch (Exception q)
            { MessageBox.Show(q.Message); }
        }
    }
}
