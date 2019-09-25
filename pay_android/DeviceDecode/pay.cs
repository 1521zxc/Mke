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
    public partial class pay : UserControl
    {
        public int se = 0;
        public pay()
        {
            InitializeComponent();
            
        }

        private void button4_Click(object sender, EventArgs e)
        {
            try {
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
                        long start = ConvertDateTimeToInt(this.dateTimePicker1.Value);
                        long end = ConvertDateTimeToInt(this.dateTimePicker2.Value);

                        this.pageIndex.Text = (int.Parse(this.pageIndex.Text) + 1).ToString();
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 81 }, { "token", Login.userInfo.token } };

                        JObject json3 = null;
                        if (se == 1)
                        {
                            json3 = new JObject { { "todaySum", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "totalCount", 1 }, { "fromTime", start }, { "toTime", end } };
                        }
                        else
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 } };
                        }
                        json2.Add("data", json3.ToString());
                        string result = string.Empty;
                        result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());

                        MainForm.getSearchPays(result);
                    }
                }
            }

              
            catch (Exception q) { MessageBox.Show(q.Message); }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            Console.WriteLine("se" + se);
            try {
                if (pageIndex.Text == "0")
                {
                    MessageBox.Show("暂无记录");
                }
                else
                {
                    if ("1" == this.pageIndex.Text)
                    {
                        MessageBox.Show("已经是首页啦");
                    }
                    else
                    {

                        long start = ConvertDateTimeToInt(this.dateTimePicker1.Value);
                        long end = ConvertDateTimeToInt(this.dateTimePicker2.Value);
                        this.pageIndex.Text = (int.Parse(this.pageIndex.Text) - 1).ToString();
                        JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 81 }, { "token", Login.userInfo.token } };
                        JObject json3 = null;
                        if (se == 1)
                        {
                            json3 = new JObject { { "todaySum", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 }, { "totalCount", 1 }, { "fromTime", start }, { "toTime", end } };
                        }
                        else
                        {
                            json3 = new JObject { { "totalCount", 1 }, { "pageNO", int.Parse(this.pageIndex.Text) - 1 } };
                        }
                        json2.Add("data", json3.ToString());
                        string result = string.Empty;
                        result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                        Console.WriteLine(result);
                        MainForm.getSearchPays(result);
                    }
                }
               
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }

        private void search_Click(object sender, EventArgs e)
        {
            se = 1;
            long start = ConvertDateTimeToInt(this.dateTimePicker1.Value);
            long end = ConvertDateTimeToInt(this.dateTimePicker2.Value);
            try
            {
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 81 }, { "token", Login.userInfo.token } };
                JObject json3 = new JObject { { "todaySum", 1 }, { "totalCount", 1 }, { "fromTime", start }, { "toTime", end } };

                json2.Add("data", json3.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                Console.WriteLine(json2.ToString());
                MainForm.getSearchPays(result);
                pageIndex.Text = "1";

            }
            catch (Exception q) { MessageBox.Show(q.Message); }

        }
        public static long ConvertDateTimeToInt(System.DateTime time)
        {
            System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1, 0, 0, 0, 0));
            long t = (time.Ticks - startTime.Ticks) / 10000;   //除10000调整为13位      
            return t;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            statement sta = new statement();
            sta.ShowDialog();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            se = 0;
            this.button2.BackColor = Color.FromArgb(255, 221, 221, 221);
            this.button2.Text = "· 流水";
            try
            {
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 81 }, { "token", Login.userInfo.token } };
                JObject json3 = new JObject { { "todaySum", 1 }, { "totalCount", 1 }};

                json2.Add("data", json3.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                Console.WriteLine(json2.ToString());
                MainForm.getSearchPays(result);
                pageIndex.Text = "1";

            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
    }
}
