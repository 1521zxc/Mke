using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace DeviceDecode
{
    public partial class ChangeDesk : Form
    {
        bool beginMove = false;//初始化鼠标位置  
        int currentXPosition;
        int currentYPosition;
        private int orderId;
        public ChangeDesk(int orderId,string deskName)
        {
           
            InitializeComponent();
            this.orderId = orderId;
            this.label3.Text = deskName;
            JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };
            JObject json3 = new JObject { { "pageSize", int.MaxValue }, { "deskStatus", 0 } };

            json2.Add("data", json3.ToString());
            string result = string.Empty;
            result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
            Console.WriteLine("json2.ToString():" + json2.ToString());
            Console.WriteLine("result" + result);
            if (result != string.Empty)
            {
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                if (jo["errorCode"].ToString().Equals("0"))
                {
                    List<DeskInfo> desks = new List<DeskInfo>();
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                    if (int.Parse(jsonObect["count"].ToString()) > 0)
                    {
                        JArray jarry = JArray.Parse(jsonObect["items"].ToString());
                        int j = 0;
                        int k = 0;
                        for (int i = 0; i < int.Parse(jsonObect["count"].ToString()); i++)
                        {
                            DeskInfo deskInfo = new DeskInfo();
                            deskInfo.ID = int.Parse(jarry[i]["ID"].ToString());
                            deskInfo.deskNO = jarry[i]["deskNO"].ToString();
                            deskInfo.deskName = jarry[i]["deskName"].ToString();
                            desks.Add(deskInfo);
                        }
                        this.comboBox1.DataSource = desks;
                        this.comboBox1.ValueMember = "deskNO";
                        this.comboBox1.DisplayMember = "deskName";
                    }
                    else
                    {
                        MessageBox.Show("暂无可用桌台");
                        this.button1.Enabled = false;
                    }
                }
                else
                {
                    MessageBox.Show(jo["message"].ToString());
                }
            }

                    
           
         
        }

        private void ChangeDesk_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                beginMove = true;
                currentXPosition = MousePosition.X;//鼠标的x坐标为当前窗体左上角x坐标  
                currentYPosition = MousePosition.Y;//鼠标的y坐标为当前窗体左上角y坐标  
            }
        }

        private void ChangeDesk_MouseUp(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                currentXPosition = 0; //设置初始状态  
                currentYPosition = 0;
                beginMove = false;
            }
        }

        private void ChangeDesk_MouseMove(object sender, MouseEventArgs e)
        {
            if (beginMove)
            {
                this.Left += MousePosition.X - currentXPosition;//根据鼠标x坐标确定窗体的左边坐标x  
                this.Top += MousePosition.Y - currentYPosition;//根据鼠标的y坐标窗体的顶部，即Y坐标  
                currentXPosition = MousePosition.X;
                currentYPosition = MousePosition.Y;
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.Close();
            this.Dispose();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try {
                string result = string.Empty;
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 93 }, { "muuid", Login.userInfo.muuid } };
                JObject json3 = new JObject { { "ID", this.orderId }, { "toDeskNO", this.comboBox1.SelectedValue.ToString() }};

                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                if (result != string.Empty)
                {
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        MessageBox.Show("转台成功");
                        JObject json4 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 48 }, { "token", Login.userInfo.token } };

                        JObject json5 = null;

                        json5 = new JObject { { "totalCount", 1 }, { "pageSize", 15 } };

                        json4.Add("data", json5.ToString());
                        string result2 = string.Empty;
                        result2 = MainForm.HttpPostData(MainForm.testurl, json4.ToString());
                        MainForm.getDesks(result2,true);
                        this.Close();
                        this.Dispose();
                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                    }
                }
            }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }
        
    }
}
