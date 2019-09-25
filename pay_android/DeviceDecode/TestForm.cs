using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.Threading;
using System.Net;
using System.IO;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using Microsoft.VisualBasic;

namespace DeviceDecode
{
    public partial class TestForm : Form
    {
        private double handlePrice2;
        public bool isvip = false;
        public int showVip = 0;
        public static int payResult = 0;
        //设备状态  关闭
        public Boolean deviceState = false;  
        public int pay = 0;
        string url = "";
        public static string code="";
        public string orderNo;

        public bool deskShow;//是否桌台管理调起
        public int qrEnable = 1;
        public int dmEnable = 0;
        public int hxEnable = 1;
        public int barEnable = 1;
        public const int USER = 0x0400;//程序起始地址
        public Boolean LedState = true;
        private bool refund = false;
        public TestForm(string orderNo,double handlePrice, double memberPrice, double realityPrice,bool desk)
        {
            InitializeComponent();
            this.deskShow = desk;
            this.label20.Visible = false;
            this.label21.Visible = false;
            this.label10.Visible = false;
            this.textBox1.Visible = false;
            SetSearchDefaultText();
            this.handleprice.Text = handlePrice.ToString();
            this.reducedprice.Text = memberPrice.ToString();
            //this.moneyText.Text = (double.Parse(handlePrice.ToString())- realityPrice).ToString();
            this.moneyText.Text = String.Format("{0:F}", (double.Parse(handlePrice.ToString()) - realityPrice));
            this.handlePrice2 = double.Parse(this.moneyText.Text);
            if ((double.Parse(handlePrice.ToString()) - realityPrice)<0)
            {
                refund = true;
                this.comboBox1.Enabled = false;
                this.pictureBox1.Enabled = false;

                this.button1.BackgroundImage = Properties.Resources.refund_pay;
            }
            this.realityprice.Text = realityPrice.ToString();
            this.orderNo = orderNo;
            PayType payType = new PayType();
            payType.id = 1;
            payType.name ="微信-小白盒";

            PayType payType2 = new PayType();
            payType2.id = 2;
            payType2.name = "支付宝-小白盒";

            PayType payType3 = new PayType();
            payType3.id = 3;
            payType3.name = "现金";

            PayType payType4 = new PayType();
            payType4.id = 10;
            payType4.name = "会员卡";

            PayType payType6 = new PayType();
            payType6.id = 4;
            payType6.name = "银联卡";

        //   PayType payType5 = new PayType();
        //    payType5.id = 5;
        //    payType5.name = "其他";

            PayType payType7 = new PayType();
            payType7.id = 12;
            payType7.name = "美团";

            PayType payType8 = new PayType();
            payType8.id = 13;
            payType8.name = "饿了么";

            List<PayType> payTypes = new List<PayType>();
          //  payTypes.Add(payType5);
            payTypes.Add(payType2);
            payTypes.Add(payType);
            payTypes.Add(payType4);
            payTypes.Add(payType6);
            payTypes.Add(payType3);
            payTypes.Add(payType8);
            payTypes.Add(payType7);

            this.comboBox1.DataSource = payTypes;
            this.comboBox1.ValueMember = "id";
            this.comboBox1.DisplayMember = "name";

            //折扣
             JObject json4 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 53 }, { "token", Login.userInfo.token } };
           //  JObject json5 = null;
            // json5 = new JObject { { "totalCount", 1 }, { "pageSize", 15 } };
            // json4.Add("data", json5.ToString());
             string result = string.Empty;
             result = MainForm.HttpPostData(MainForm.testurl, json4.ToString());
             if (result != string.Empty)
             {
                Console.WriteLine(result);
                  JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                  if (jo["errorCode"].ToString().Equals("0"))
                  {
                      JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                     
                      if (int.Parse(jsonObect["count"].ToString()) > 0)
                      { 
                          JArray jarry = JArray.Parse(jsonObect["items"].ToString());
                          List<Discount> discounts = new List<Discount>();
                          Discount discount = new Discount();
                          discount.name = "请选择";
                          discount.discount = -1;
                          discounts.Add(discount);
                          for (int i = 0; i < int.Parse(jsonObect["count"].ToString()); i++)
                          {
                              Discount discount2 = new Discount();
                              discount2.discount = double.Parse(jarry[i]["discount"].ToString());
                              discount2.name = jarry[i]["name"].ToString();
                              discounts.Add(discount2);
                          }
                        
                          this.comboBox2.DataSource = discounts;
                          this.comboBox2.ValueMember = "discount";
                          this.comboBox2.DisplayMember = "name";
                      }
                  }
              }
            this.comboBox2.SelectedIndexChanged += new System.EventHandler(this.comboBox2_SelectedIndexChanged);
            this.moneyText.TextChanged += new System.EventHandler(this.moneyText_TextChanged);


        }
        protected override void DefWndProc(ref Message msg)
        {   //扫码调用
           
                if (msg.Msg == USER + 123)//receive code info from the 532 dll
                {
                if (pay == 0)
                {
                    PPDriver.setQRable(false);
                    PPDriver.setDMable(false);
                    PPDriver.setQRable(false);
                    PPDriver.setHXable(false);

                    byte[] wpbuf = new byte[((int)msg.LParam)];
                    Marshal.Copy(msg.WParam, wpbuf, 0, ((int)msg.LParam));
                    string strBC = System.Text.Encoding.GetEncoding("UTF-8").GetString(wpbuf, 0, ((int)msg.LParam));
                    this.codeInfo.Text = strBC.Trim();
                    Console.Write(this.codeInfo.Text);
                    code = this.codeInfo.Text;
                    pay = 1;

                    Thread.Sleep(500);

                    if (this.dmEnable == 1)
                    {

                        PPDriver.setDMable(true);
                    }
                    if (this.qrEnable == 1)
                    {

                        PPDriver.setQRable(true);
                    }
                    if (this.barEnable == 1)
                    {

                        PPDriver.setQRable(true);
                    }
                    if (this.hxEnable == 1)
                    {

                        PPDriver.setHXable(true);
                    }

                }
                else
                {
                    MessageBox.Show("已经获得付款码啦");
                }
            }
            else
            {
                base.DefWndProc(ref msg);
            }


        }
        private void TestForm_Load(object sender, EventArgs e)
        {
            this.timerCheck.Enabled = true;
            this.timerCheck.Interval = 1000;
            //通过消息得到解码信息 获取本地权限
            PPDriver.GetAppHandle(this.Handle);
            this.qrDecode.Checked = true;
            this.dmDecode.Checked = false;
            this.hxDecode.Checked = false;
            this.barDecode.Checked = true;
        }
        private void qrDecode_CheckedChanged(object sender, EventArgs e)
        {
            if (this.qrDecode.Checked == true)
            {
                this.qrEnable = 1;
            }
            else
            {
                this.qrEnable = 0;
            }
            PPDriver.setQRable(Convert.ToBoolean(this.qrEnable));
        }
        //定时器 定时查看设备连接状态
        private void timerCheck_Tick(object sender, EventArgs e)
        {
            int flag = PPDriver.GetDevice();

            if (flag == 1)
            {
                if (!deviceState)
                {
                   
                    flag = PPDriver.StartDevice();

                    if (flag == 1)
                    {
                        //第三步：设置打开蜂鸣器，DLL默认不打开
                        PPDriver.SetBeep(true);
                        //设置一维                
                        PPDriver.setQRable(Convert.ToBoolean(barEnable));
                        //设置qr
                        PPDriver.setQRable(Convert.ToBoolean(qrEnable));
                        //设置dm
                        PPDriver.setDMable(Convert.ToBoolean(dmEnable));
                        //设置启动成功蜂鸣
                        PPDriver.SetBeepTime(100);
                      
                        //deviceState = true;
                        this.label13.Text = "设备已连接";
                    }
                    else if (flag == -1)
                    {
                        this.label13.Text = "设备已启动";
                    }
                    else if (flag == -2)
                    {
                        this.label13.Text = "设备已断开";
                    }
                    else if (flag == -3)
                    {
                        this.label13.Text = "设备已初始化失败";
                    }
                }
                if (deviceState)
                {
                    
                    //deviceState = false;
                    PPDriver.ReleaseLostDevice();
                    this.label13.Text = "设备已断开";
                }
            }
            else
            {
                if (deviceState)
                {   
                    Console.WriteLine("3:" + deviceState);
                    deviceState = false;
                    PPDriver.ReleaseLostDevice();
                    this.label13.Text = "设备已断开";
                }
            }
        }
        //点击“获取解码信息”按钮，通过DLL里的GetDecodeString函数取得码值
       // private void buttonRead_Click(object sender, EventArgs e)
       // {
       //     if (deviceState)
        //    {
       //         int Length = 1024;
        //        StringBuilder tempStr = new StringBuilder("", Length);

        //        PPDriver.setQRable(false);//为防止重复信息误读 关闭解码
        //        PPDriver.setQRable(false);
        //        PPDriver.GetDecodeString(tempStr, out Length);
        //        if (Length > 0)
        //        {
         //           this.textBox1.AppendText(tempStr.ToString());
        //        }

        //        Thread.Sleep(1000);
        //        PPDriver.setQRable(true);
        //        PPDriver.setQRable(true);//传输解码信息完毕 开启解码
        //    }
        //    else
         //   {
        //        MessageBox.Show("设备已断开");
         //   }
        //}

        //private void Clear_Click(object sender, EventArgs e)
        //{
        //    StringBuilder tempStr = new StringBuilder();
        //    tempStr.Length = 0;
         //   this.codeInfo.Text = tempStr.ToString();
        //    this.textBox1.Text = tempStr.ToString();
            
       // }
        
        private void TestForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (deviceState == true)
            {
                PPDriver.ReleaseDevice();
            }
            else
            {
                PPDriver.ReleaseLostDevice();
            }
        }

        private void dmDecode_CheckedChanged(object sender, EventArgs e)
        {
            if (this.dmDecode.Checked == true)
            {
                this.dmEnable = 1;
            }
            else
            {
                this.dmEnable = 0;
            }
            PPDriver.setDMable(Convert.ToBoolean(this.dmEnable));
        }

        private void hxDecode_CheckedChanged(object sender, EventArgs e)
        {
            if (this.hxDecode.Checked == true)
            {
                this.hxEnable = 1;
            }
            else
            {
                this.hxEnable = 0;
            }
            PPDriver.setHXable(Convert.ToBoolean(this.hxEnable));
        }

        private void barDecode_CheckedChanged(object sender, EventArgs e)
        {
            if (this.barDecode.Checked == true)
            {
                this.barEnable = 1;
            }
            else
            {
                this.barEnable = 0;
            }

            PPDriver.setQRable(Convert.ToBoolean(this.barEnable));
        }

        private void button1_Click(object sender, EventArgs e)
        {
            //try {

                //string code = this.codeInfo.Text;
                double price = double.Parse(this.moneyText.Text);
                if (!refund)
                {
                    if (int.Parse(this.comboBox1.SelectedValue.ToString()) == 2)
                    {
                        url = "http://pay.jqepay.com/ali/micro/create";
                      
                       
                        if (!this.label13.Text.Equals("设备已连接") && !this.label13.Text.Equals("设备已启动"))
                        {
                            MessageBox.Show(this.label13.Text);
                        }
                        else
                        {
                            if (pay == 1)
                            {

                                //扫码支付
                                string result = string.Empty;
                                string param = string.Format("orderNO={0}&money={1}&authCode={2}&muuid={3}", orderNo, price, code ,Login.userInfo.muuid);
                                result = MainForm.HttpPostData(url, param);
                                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                                if (jo["errorCode"].ToString().Equals("0"))
                                {

                                    MessageBox.Show("支付成功");
                                    this.Close();
                                    this.Dispose();
                                }
                                else
                                {
                                    MessageBox.Show(jo["message"].ToString());
                                }
                            }
                            else
                            {
                                MessageBox.Show("请扫码");
                            }

                        }
                    }
                    else if (int.Parse(this.comboBox1.SelectedValue.ToString()) == 1)
                    {
                        url = "http://pay.jqepay.com/weixin/micro/create";
                     
                        if (!this.label13.Text.Equals("设备已连接") && !this.label13.Text.Equals("设备已启动"))
                        {
                            MessageBox.Show("设备未安装");
                        }
                        else
                        {

                            if (pay == 1)
                            {
                                //扫码支付
                                string result = string.Empty;
                                string param = string.Format("orderNO={0}&money={1}&authCode={2}&muuid={3}", orderNo, price, code, Login.userInfo.muuid);
                                result = MainForm.HttpPostData(url, param);
                                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                                if (jo["errorCode"].ToString().Equals("0"))
                                {

                                    MessageBox.Show("支付成功");
                                    this.Close();
                                    this.Dispose();

                                }
                                else
                                {
                                    MessageBox.Show(jo["message"].ToString());
                                }
                            }
                            else
                            {
                                MessageBox.Show("请扫码");
                            }
                        }
                    }
                    else if (int.Parse(this.comboBox1.SelectedValue.ToString()) == 3)
                    {

                        url = "http://pay.jqepay.com/cash/pay";
                        if (this.textBox1.Text.Equals(""))
                        {
                            MessageBox.Show("现金不能为空");
                        }
                        else
                        {
                            double cashPrice = 0;
                            try
                            {
                                cashPrice = double.Parse(this.textBox1.Text);
                            }
                            catch (Exception q) { MessageBox.Show("现金类型错误"); return; }

                            if (cashPrice < price)
                            {
                                MessageBox.Show("现金不能小于收款金额"); return;

                            }

                            string result = string.Empty;
                            string param = string.Format("orderNO={0}&money={1}&muuid={2}&payType={3}", orderNo, price, Login.userInfo.muuid, int.Parse(this.comboBox1.SelectedValue.ToString()));
                            result = MainForm.HttpPostData(url, param);
                            if (result != string.Empty)
                            {
                                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                                if (jo["errorCode"].ToString().Equals("0"))
                                {
                                    JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 91 }, { "muuid", Login.userInfo.muuid } };
                                    JObject json3 = new JObject { { "orderNO", orderNo } };

                                    json2.Add("data", json3.ToString());

                                    string result2 = string.Empty;
                                    result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                                    MainForm.printPayCp(result2, double.Parse(this.label8.Text), double.Parse(this.textBox1.Text),this.label24.Text);
                                    MessageBox.Show("支付成功");

                                    if (deskShow)
                                    {
                                        JObject json4 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

                                        JObject json5 = null;

                                        json5 = new JObject { { "totalCount", 1 }, { "pageSize", 15 } };

                                        json4.Add("data", json5.ToString());
                                        string result3 = string.Empty;
                                        result3 = MainForm.HttpPostData(MainForm.testurl, json4.ToString());
                                        MainForm.getDesks(result3, true);
                                    }
                                    else
                                    {
                                        JObject json6 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 90 }, { "token", Login.userInfo.token } };
                                        JObject json7 = new JObject { { "totalCount", 1 } };

                                        json6.Add("data", json7.ToString());
                                        Console.WriteLine(json6.ToString());
                                        string result6 = string.Empty;                                    
                                        MainForm.getOrders(json6.ToString());
                                    }
                                    this.Close();
                                    this.Dispose();

                                }
                                else
                                {
                                    MessageBox.Show(jo["message"].ToString());
                                }

                            }
                        }

                    }
                    else if ( int.Parse(this.comboBox1.SelectedValue.ToString()) == 12|| int.Parse(this.comboBox1.SelectedValue.ToString()) == 13)
                    {
                        url = "http://pay.jqepay.com/cash/pay";

                        string result = string.Empty;
                        string param = string.Format("orderNO={0}&money={1}&muuid={2}&payType={3}", orderNo, price, Login.userInfo.muuid, int.Parse(this.comboBox1.SelectedValue.ToString()));
                        Console.WriteLine("param" + param);
                        result = MainForm.HttpPostData(url, param);
                        Console.WriteLine(132);
                        if (result != string.Empty)
                        {
                            JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                            if (jo["errorCode"].ToString().Equals("0"))
                            {

                                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 91 }, { "muuid", Login.userInfo.muuid } };
                                JObject json3 = new JObject { { "orderNO", orderNo } };

                                json2.Add("data", json3.ToString());

                                string result2 = string.Empty;
                                result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                                MainForm.printPayCp(result2, 0, 0, this.label24.Text);
                                MessageBox.Show("支付成功");
                                if (deskShow)
                                {
                                    JObject json4 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

                                    JObject json5 = null;

                                    json5 = new JObject { { "totalCount", 1 }, { "pageSize", 15 } };

                                    json4.Add("data", json5.ToString());
                                    string result3 = string.Empty;
                                    result3 = MainForm.HttpPostData(MainForm.testurl, json4.ToString());
                                    MainForm.getDesks(result3, true);
                                }
                                else
                                {
                                    JObject json6 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 90 }, { "token", Login.userInfo.token } };
                                    JObject json7 = new JObject { { "totalCount", 1 } };

                                    json6.Add("data", json7.ToString());
                                    Console.WriteLine(json6.ToString());
                                    string result6 = string.Empty;
                                    MainForm.getOrders(json6.ToString());
                                }
                                this.Close();
                                this.Dispose();

                            }
                            else
                            {
                                MessageBox.Show(jo["message"].ToString());
                            }

                        }

                    }
                    else if (int.Parse(this.comboBox1.SelectedValue.ToString()) == 4)
                    {
                        url = "http://pay.jqepay.com/cash/pay";

                        string result = string.Empty;
                        string param = string.Format("orderNO={0}&money={1}&muuid={2}&payType={3}", orderNo, price, Login.userInfo.muuid, int.Parse(this.comboBox1.SelectedValue.ToString()));
                        result = MainForm.HttpPostData(url, param);
                        if (result != string.Empty)
                        {
                            JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                            if (jo["errorCode"].ToString().Equals("0"))
                            {

                                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 91 }, { "muuid", Login.userInfo.muuid } };
                                JObject json3 = new JObject { { "orderNO", orderNo } };

                                json2.Add("data", json3.ToString());

                                string result2 = string.Empty;
                                result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                                MainForm.printPayCp(result2, 0, 0, this.label24.Text);
                                MessageBox.Show("支付成功");
                                if (deskShow)
                                {
                                    JObject json4 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

                                    JObject json5 = null;

                                    json5 = new JObject { { "totalCount", 1 }, { "pageSize", 15 } };

                                    json4.Add("data", json5.ToString());
                                    string result3 = string.Empty;
                                    result3 = MainForm.HttpPostData(MainForm.testurl, json4.ToString());
                                    MainForm.getDesks(result3, true);
                                }
                                else
                                {
                                    JObject json6 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 90 }, { "token", Login.userInfo.token } };
                                    JObject json7 = new JObject { { "totalCount", 1 } };

                                    json6.Add("data", json7.ToString());
                                    Console.WriteLine(json6.ToString());
                                    string result6 = string.Empty;
                                    MainForm.getOrders(json6.ToString());
                                }
                                this.Close();
                                this.Dispose();

                            }
                            else
                            {
                                MessageBox.Show(jo["message"].ToString());
                            }

                        }

                    }
                    else if (int.Parse(this.comboBox1.SelectedValue.ToString()) == 10)
                    {
                        MessageBox.Show("暂不支持，敬请期待...");
                    }

                }
                else
                {
                    
                    string url = "http://pay.jqepay.com/refund/atonce";
                    if (double.Parse(this.moneyText.Text) < 0)
                    {
                        string str = Interaction.InputBox("请输入退款密码", "退款", "", -1, -1);
                        if (str.Length > 0)
                        {
                        //退款密码接口
                        JObject json211 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 29 }, { "token", Login.userInfo.token } };
                        JObject json311 = new JObject { { "orderNO", orderNo }, { "refundPwd", str } };

                        json211.Add("data", json311.ToString());

                        string result31 = string.Empty;
                        result31 = MainForm.HttpPostData(MainForm.testurl.ToString(), json211.ToString());
                        if (result31 != string.Empty)
                        {
                            JObject jo = (JObject)JsonConvert.DeserializeObject(result31);
                            if (jo["errorCode"].ToString().Equals("0"))
                            {
                                this.moneyText.Text = this.moneyText.Text.Substring(this.moneyText.Text.LastIndexOf("-") + 1);
                                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 91 }, { "muuid", Login.userInfo.muuid } };
                                JObject json3 = new JObject { { "orderNO", orderNo } };

                                json2.Add("data", json3.ToString());

                                string result2 = string.Empty;
                                result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());

                                string result = string.Empty;
                                string param = string.Format("orderNO={0}&muuid={1}&refundAmt={2}", orderNo, Login.userInfo.muuid, double.Parse(this.moneyText.Text));
                                Console.WriteLine(param);
                                result = MainForm.HttpPostData(url, param);
                                if (result != string.Empty)
                                {
                                    JObject jo2 = (JObject)JsonConvert.DeserializeObject(result);
                                    if (jo2["errorCode"].ToString().Equals("0"))
                                    {

                                        MainForm.printRefundCp(result2, double.Parse(this.moneyText.Text));
                                        MessageBox.Show("退款成功");
                                        if (deskShow)
                                        {
                                            JObject json4 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

                                            JObject json5 = null;

                                            json5 = new JObject { { "totalCount", 1 }, { "pageSize", 15 } };

                                            json4.Add("data", json5.ToString());
                                            string result3 = string.Empty;
                                            result3 = MainForm.HttpPostData(MainForm.testurl, json4.ToString());
                                            MainForm.getDesks(result3, true);
                                        }
                                        this.Close();
                                        this.Dispose();
                                    }
                                    else
                                    {
                                        MessageBox.Show(jo["message"].ToString());
                                    }
                                }
                            }
                            else
                            {
                                MessageBox.Show("密码错误");
                            }
                        }
                      }
                        
                    }
                    else
                    {
                        MessageBox.Show("金额错误");
                    }

                }

           // }
           // catch (Exception q) { MessageBox.Show(q.Message); }

        }
      
        /* 无边框窗体移动开始 */
        private void TestForm_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                beginMove = true;
                currentXPosition = MousePosition.X;//鼠标的x坐标为当前窗体左上角x坐标  
                currentYPosition = MousePosition.Y;//鼠标的y坐标为当前窗体左上角y坐标  
            }
        }
       
        private void TestForm_MouseMove(object sender, MouseEventArgs e)
        {
            if (beginMove)
            {
                this.Left += MousePosition.X - currentXPosition;//根据鼠标x坐标确定窗体的左边坐标x  
                this.Top += MousePosition.Y - currentYPosition;//根据鼠标的y坐标窗体的顶部，即Y坐标  
                currentXPosition = MousePosition.X;
                currentYPosition = MousePosition.Y;
            }
        }

        private void TestForm_MouseUp(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                currentXPosition = 0; //设置初始状态  
                currentYPosition = 0;
                beginMove = false;
            }
        }
        /* 无边框窗体移动结束 */
        private void button2_Click(object sender, EventArgs e)
        {
           // if (MessageBox.Show("您确定要退出骏骐支付系统吗？", "确认退出", MessageBoxButtons.OKCancel, MessageBoxIcon.Question, MessageBoxDefaultButton.Button2) == DialogResult.OK)
           // {              
                this.Close();
                this.Dispose();
               // Application.Exit();
           // }
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

        private void panel1_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                beginMove = true;
                currentXPosition = MousePosition.X;//鼠标的x坐标为当前窗体左上角x坐标  
                currentYPosition = MousePosition.Y;//鼠标的y坐标为当前窗体左上角y坐标  
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
        private void SetSearchDefaultText()
        {
            viptext.Text = "卡号/手机号";
            viptext.Font = new Font("微软雅黑", 14);
            viptext.ForeColor = Color.Gray;
        }

        private void viptext_Enter(object sender, EventArgs e)
        {
            if (viptext.Text == "卡号/手机号")
            {
                viptext.Text = "";
                viptext.ForeColor = Color.Black;
            }
        }

        private void viptext_Leave(object sender, EventArgs e)
        {
            if (viptext.Text == "")
            {
                viptext.Text = "卡号/手机号";
                viptext.Font = new Font("微软雅黑", 14);
                viptext.ForeColor = Color.Gray;
            }
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            this.label8.Text = "0";
            this.textBox1.Text = "";
            if (this.comboBox1.SelectedValue.ToString().Equals("3"))
            {
                
                this.label10.Visible = true;
                this.textBox1.Visible = true;
            }
            else
            {
                this.label10.Visible = false;
                this.textBox1.Visible = false;
            }
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            try
            {
                if (!this.textBox1.Text.Equals(""))
                {
                    this.label8.Text = String.Format("{0:F}", (double.Parse(this.textBox1.Text) - double.Parse(this.moneyText.Text)));
                    if (double.Parse(this.label8.Text) < 0)
                    {
                        this.label8.Text = 0.ToString();
                    }

                }
                else
                {
                    this.label8.Text = 0.ToString();
                }
            }
            catch (Exception q) { MessageBox.Show("现金类型错误"); }

        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {
            if (showVip == 1)
            {
                this.pictureBox1.BackgroundImage = Properties.Resources.down;
                this.Size = new Size(400, 398);
                this.pictureBox1.Size = new Size(82, 38);
                this.pictureBox1.Location = new Point(161, 353);
                showVip = 0;
            }
            else if(showVip == 0)
            {
                this.pictureBox1.BackgroundImage = Properties.Resources.up;
                this.pictureBox1.Size = new Size(69,38);
                this.pictureBox1.Location = new Point(170, 353);
                this.Size = new Size(400, 599);
                showVip = 1;
            }
    
           
        }

        private void vipyz_Click(object sender, EventArgs e)
        {
           try {
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 47 }, { "muuid",Login.userInfo.muuid } };
                JObject json3 = new JObject { { "checkNO", this.viptext.Text }};

                json2.Add("data", json3.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                if (result != string.Empty)
                {
                    Console.WriteLine("vip"+result);
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                if (jo["errorCode"].ToString().Equals("0"))
                {
                  
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                    this.label19.Text = jsonObect["userName"].ToString();
                    if (jsonObect["flag"] != null && jsonObect["flag"].ToString().Equals("1"))
                    {
                        isvip = true;
                        this.moneyText.Text = (double.Parse(this.reducedprice.Text) - double.Parse(this.realityprice.Text)).ToString();
                        if (this.comboBox1.SelectedValue.ToString().Equals("3"))
                        {
                            textBox1_TextChanged(null, null);
                        }
                        moneyText_TextChanged(null, null);
                            if (double.Parse(this.comboBox2.SelectedValue.ToString()) != -1)
                            {
                                // Console.WriteLine(this.comboBox2.SelectedValue.ToString());
                                this.moneyText.Text = String.Format("{0:F}", (double.Parse(this.moneyText.Text) * (double.Parse(this.comboBox2.SelectedValue.ToString()) * 0.1)));
                            }
                        }
                    else
                    {
                        MessageBox.Show("该会员已停用");
                    }
                    if (jsonObect["type"] != null && jsonObect["type"].ToString().Equals("1"))
                    {
                        this.label20.Visible = true;
                        this.label21.Visible = true;
                        this.label21.Text = jsonObect["price"].ToString();
                    }
                    else
                    {
                        this.label20.Visible = false;
                        this.label21.Visible = false;
                    }
                } else if (jo["errorCode"].ToString().Equals("0"))
                {
                    MessageBox.Show("非本店会员");
                }
                else
                {
                    MessageBox.Show(jo["message"].ToString());
                }
                }
           }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }

        private void moneyText_TextChanged(object sender, EventArgs e)
        {
            try { 
            if (!refund)
            {
                if (this.moneyText.Text != "")
                {
                   
                    if (int.Parse(this.comboBox1.SelectedValue.ToString()) == 3 && this.textBox1.Text != "")
                    {
                        this.label8.Text = String.Format("{0:F}", (double.Parse(this.textBox1.Text) - double.Parse(this.moneyText.Text)));
                        if (double.Parse(this.label8.Text) < 0)
                        {
                            this.label8.Text = 0.ToString();
                        }
                    }
                    if (double.Parse(this.handleprice.Text) >= double.Parse(this.moneyText.Text))
                    {
                        this.label24.Text = String.Format("{0:F}", (double.Parse(this.handleprice.Text) - double.Parse(this.moneyText.Text)));
                    }

                   
                }
                else
                {
                    this.label24.Text = this.handleprice.Text;
                }
            }
            }
            catch (Exception q) { MessageBox.Show(q.Message); }

        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (double.Parse(this.comboBox2.SelectedValue.ToString()) != -1)
            {
                if (isvip)
                {
                    this.moneyText.Text = String.Format("{0:F}", (double.Parse(this.reducedprice.Text) * (double.Parse(this.comboBox2.SelectedValue.ToString()) * 0.1)));
                }
                else
                {
                    this.moneyText.Text = String.Format("{0:F}", (handlePrice2 * (double.Parse(this.comboBox2.SelectedValue.ToString()) * 0.1)));
                }
        
               
            }
            else
            {

                if (isvip)
                {
                    this.moneyText.Text = this.reducedprice.Text;
                }
                else
                {
                    this.moneyText.Text = String.Format("{0:F}", handlePrice2);
                }
            }
        }
    }
}
