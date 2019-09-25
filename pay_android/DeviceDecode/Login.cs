using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Windows.Forms;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;


namespace DeviceDecode
{
    public partial class Login : Form
    {
        bool beginMove = false;//初始化鼠标位置  
        int currentXPosition;
        int currentYPosition;
        public string testurl = "https://api.jqepay.com";
        public string url = "https://api.jqepay.com";
        public static UserInfo userInfo=new UserInfo();
        public Login()
        {
            InitializeComponent();
            this.pictureBox1.Visible = false;
        }

        //登陆
        private void button1_Click(object sender, EventArgs e)
        {
            try {
                string result = string.Empty;
                this.pictureBox1.Visible = true;
                string mobile = this.username.Text;
                string password = this.password.Text;

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 31 } };
                JObject json3 = new JObject { { "password", password }, { "mobile", mobile }, { "app", "30" } };

                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                result = HttpPostData(testurl.ToString(), json2.ToString());
                if (result!= string.Empty)
                {
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);

                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());

                        userInfo.id = int.Parse(jsonObect["ID"] == null ? "" : jsonObect["ID"].ToString());
                        userInfo.userID = int.Parse(jsonObect["userID"] == null ? "" : jsonObect["userID"].ToString());
                        userInfo.mobile = jsonObect["mobile"] == null ? "" : jsonObect["mobile"].ToString();
                        userInfo.brandName = jsonObect["brandName"] == null ? "" : jsonObect["brandName"].ToString();
                        userInfo.username = jsonObect["username"] == null ? "" : jsonObect["username"].ToString();
                        userInfo.iconURL = jsonObect["iconURL"] == null ? "" : jsonObect["iconURL"].ToString();
                        userInfo.coverURL = jsonObect["coverURL"] == null ? "" : jsonObect["coverURL"].ToString();
                        userInfo.token = jsonObect["token"] == null ? "" : jsonObect["token"].ToString();
                        userInfo.muuid = jsonObect["muuid"] == null ? "" : jsonObect["muuid"].ToString();
                        userInfo.address = jsonObect["address"] == null ? "" : jsonObect["address"].ToString();
                        userInfo.cusPhone = jsonObect["cusPhone"] == null ? "" : jsonObect["cusPhone"].ToString();
                        userInfo.isClose= jsonObect["isClose"] == null ? 1 : int.Parse(jsonObect["isClose"].ToString());

                        this.Hide();
                        MainForm mainForm = new MainForm();
                        mainForm.Show();

                      
                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                        this.pictureBox1.Visible = false;
                    }
                }
                
            }
            catch (Exception q)
            {
                MessageBox.Show(q.Message);
            }


        }
        public string HttpPostData(string url, string param)
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
                MessageBox.Show("服务器异常"+e.Message);
            }
            return result;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.Close();
            this.Dispose();
            Application.Exit();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.WindowState = FormWindowState.Minimized;
        }

        private void Login_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                beginMove = true;
                currentXPosition = MousePosition.X;//鼠标的x坐标为当前窗体左上角x坐标  
                currentYPosition = MousePosition.Y;//鼠标的y坐标为当前窗体左上角y坐标  
            }
        }

        private void Login_MouseMove(object sender, MouseEventArgs e)
        {
            if (beginMove)
            {
                this.Left += MousePosition.X - currentXPosition;//根据鼠标x坐标确定窗体的左边坐标x  
                this.Top += MousePosition.Y - currentYPosition;//根据鼠标的y坐标窗体的顶部，即Y坐标  
                currentXPosition = MousePosition.X;
                currentYPosition = MousePosition.Y;
            }
        }

        private void Login_MouseUp(object sender, MouseEventArgs e)
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
            username.Text = "请输入账号";
            username.Font = new Font("微软雅黑", 14);
            username.ForeColor = Color.Gray;

            password.Text = "请输入密码";
            password.Font = new Font("微软雅黑", 14);
            password.ForeColor = Color.Gray;

        }

        private void username_Enter(object sender, EventArgs e)
        {
            if (username.Text == "请输入账号")
            {
                username.Text = "";
                username.ForeColor = Color.Black;
            }
        }

        private void password_Enter(object sender, EventArgs e)
        {
            if (password.Text == "请输入密码")
            {
                password.Text = "";
                password.ForeColor = Color.Black;
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

        private void username_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)//如果输入的是回车键  
            {
                this.button1_Click(sender, e);//触发button事件  
            }
        }

        private void password_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)//如果输入的是回车键  
            {
                this.button1_Click(sender, e);//触发button事件  
            }
        }
    }
}
