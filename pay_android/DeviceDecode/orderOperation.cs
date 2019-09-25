using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows.Forms;


namespace DeviceDecode
{
    public partial class orderOperation : Form
    {
        public static desk d = new desk();
        private string orderNo;
        private int orderId;
        IList<Cp> orderCp = new List<Cp>();
        List<Cp> addCp = new List<Cp>();
  
        List<DetailItem> removeCp = new List<DetailItem>();
        bool beginMove = false;//初始化鼠标位置  
        int currentXPosition;
        int currentYPosition;
        public orderOperation()
        {
            InitializeComponent();
            this.Size = new Size(624, 535);
            try
            {
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 4 }, { "muuid", Login.userInfo.muuid } };
                JObject json3 = new JObject { { "cataIndex", 0 } };

                json2.Add("data", json3.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                if (result != string.Empty)
                {
                    Console.WriteLine("menu" + result);
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        List<Menu> menus = new List<Menu>();
                        List<Menu> menus2 = new List<Menu>();
                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                        if (int.Parse(jsonObect["count"].ToString()) > 0)
                        {
                            JArray menuArray = JArray.Parse(jsonObect["items"].ToString());
                            Menu mm = new Menu();
                            mm.id = -1;
                            mm.name = "选择分类";
                            menus.Add(mm);
                            for (int i = 0; i < menuArray.Count; i++)
                            {
                                Menu m = new Menu();
                                m.id = int.Parse(menuArray[i]["ID"].ToString());
                                m.name = menuArray[i]["name"].ToString();
                                menus.Add(m);
                                menus2.Add(m);

                            }


                            this.comboBox2.DataSource = menus2;
                            this.comboBox2.ValueMember = "id";
                            this.comboBox2.DisplayMember = "name";
                            this.comboBox2.SelectedIndexChanged += new System.EventHandler(this.comboBox2_SelectedIndexChanged);

                            if (jsonObect["products"] != null)
                            {
                                List<Cp> cps = new List<Cp>();
                                JArray cpArray = JArray.Parse(jsonObect["products"].ToString());
                                for (int i = 0; i < cpArray.Count; i++)
                                {
                                    Cp cp = new Cp();
                                    cp.id = int.Parse(cpArray[i]["ID"].ToString());
                                    cp.name = cpArray[i]["name"].ToString();
                                    cp.price = double.Parse(cpArray[i]["price"].ToString());
                                    if (int.Parse(cpArray[i]["sellStatus"].ToString()) == 0)
                                    {
                                        cp.sellStatus = "未销售";
                                    }
                                    else if (int.Parse(cpArray[i]["sellStatus"].ToString()) == -1)
                                    {
                                        cp.sellStatus = "停售";
                                    }
                                    else if (int.Parse(cpArray[i]["sellStatus"].ToString()) == 1)
                                    {
                                        cp.sellStatus = "销售中";
                                    }
                                    cp.memberPrice = double.Parse(cpArray[i]["vipPrice"].ToString());
                                    cps.Add(cp);
                                }

                                this.dataGridView1.DataSource = cps;

                            }
                        }
                       
                    }

                }

                orderNo = MainForm.orderNo;
             
                JObject json4 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 86 }, { "muuid", Login.userInfo.muuid } };
                JObject json5 = new JObject { { "orderNO", orderNo } };

                json4.Add("data", json5.ToString());

                string result2 = string.Empty;
                result2 = MainForm.HttpPostData(MainForm.testurl.ToString(), json4.ToString());
                if (result2 != string.Empty)
                {
                    Console.WriteLine("result2"+ result2);
                    JObject jo = (JObject)JsonConvert.DeserializeObject(result2);
                    if (jo["errorCode"].ToString().Equals("0"))
                    {
                        JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                        JArray orderCpArray = JArray.Parse(jsonObect["attached"].ToString());
                        //    List<Cp> cps = new List<Cp>();
                        orderId = int.Parse(jsonObect["ID"].ToString());
                        for (int j = 0; j < orderCpArray.Count; j++)
                        {
                            Cp cp = new Cp();
                            cp.id = int.Parse(orderCpArray[j]["ID"].ToString());
                            cp.ODID = int.Parse(orderCpArray[j]["ODID"].ToString());
                            List<CpAttribution> attrs = new List<CpAttribution>();
                            if (orderCpArray[j]["attrs"] != null)
                            {
                                string attr = "";
                                JArray attrArray = JArray.Parse(orderCpArray[j]["attrs"].ToString());
                              
                                for (int k = 0; k < attrArray.Count; k++)
                                {
                                    CpAttribution a = new CpAttribution();
                                    a.id= int.Parse(attrArray[k]["ID"].ToString());
                                    a.text= attrArray[k]["text"].ToString();
                                    if (attrArray.Count - 1 == k)
                                    {
                                        attr += attrArray[k]["text"].ToString();
                                    }
                                    else
                                    {
                                        attr += attrArray[k]["text"].ToString() + ",";
                                    }
                                    attrs.Add(a);
                                }
                                cp.name = orderCpArray[j]["name"].ToString() + "(" + attr + ")";
                            }
                            else
                            {
                                cp.name = orderCpArray[j]["name"].ToString();
                            }
                            cp.attrs = attrs;
                            cp.count = int.Parse(orderCpArray[j]["count"].ToString());
                            cp.price = double.Parse(orderCpArray[j]["price"].ToString());
                            if (orderCpArray[j]["delFlag"] != null && int.Parse(orderCpArray[j]["delFlag"].ToString()) == -1)
                            {

                            }
                            else
                            {
                               // int iflag = 0;
                               // foreach (Cp c in orderCp)
                               // {
                               //     if (c.id == cp.id)
                               //     {
                               //         if (cp.attrs != null)
                               //         {
                               //             if (c.attrs != null)
                               //             {
                               //                 var exp1 = c.attrs.Where(a => cp.attrs.Exists(t => a.text.Contains(t.text))).ToList();
                               //                 if (exp1.Count > 0 && exp1.Count == c.attrs.Count)
                               //                 {
                               //                     iflag = 1;
                               //                     c.count += cp.count;
                               //                 }
                               //
                               //             }
                               //         }
                               //         else
                               //         {
                               //            iflag = 1;
                               //             c.count += cp.count;
                               //        }

                               //     }

                              //  }
                             //   if (iflag != 1)
                             //   {
                                    orderCp.Add(cp);
                             //   }
                              
                            }
                           
                        }
                        this.dataGridView3.DataSource = orderCp;
                    }
                    else
                    {
                        MessageBox.Show(jo["message"].ToString());
                    }
                }
          } catch (Exception e) { MessageBox.Show(e.Message); }
           
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
            this.Dispose();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.WindowState = FormWindowState.Minimized;
        }

        private void orderOperation_MouseLeave(object sender, EventArgs e)
        {

        }

        private void orderOperation_MouseMove(object sender, MouseEventArgs e)
        {
            if (beginMove)
            {
                this.Left += MousePosition.X - currentXPosition;//根据鼠标x坐标确定窗体的左边坐标x  
                this.Top += MousePosition.Y - currentYPosition;//根据鼠标的y坐标窗体的顶部，即Y坐标  
                currentXPosition = MousePosition.X;
                currentYPosition = MousePosition.Y;
            }
        }

        private void orderOperation_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                beginMove = true;
                currentXPosition = MousePosition.X;//鼠标的x坐标为当前窗体左上角x坐标  
                currentYPosition = MousePosition.Y;//鼠标的y坐标为当前窗体左上角y坐标  
            }
        }

        private void orderOperation_MouseUp(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                currentXPosition = 0; //设置初始状态  
                currentYPosition = 0;
                beginMove = false;
            }
        }

       

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
           
        }
        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            //  MessageBox.Show(this.comboBox2.SelectedValue.ToString());
           //  try
            // {
                this.Size = new Size(624, 535);
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 2 }, { "muuid", Login.userInfo.muuid } };
                    JObject json3 = new JObject { { "cataID", int.Parse(this.comboBox2.SelectedValue.ToString()) } };

                    json2.Add("data", json3.ToString());
                    string result = string.Empty;
                    result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());

                    getCps(result);

           // }
          //  catch (Exception q) { MessageBox.Show(q.Message); }

        }

        private void button5_Click(object sender, EventArgs e)
        {
           
            int flag = 0;
            ArrayList checkFlag = new ArrayList();
            List<CpAttribution> attrs = new List<CpAttribution>();
            bool check = false;
            int i = 0;
            Cp cp = new Cp();
            Cp cp2 = new Cp();
            ProductItem productItem = new ProductItem();
            string attrname = "";
            foreach (Control c in panel1.Controls)
            {
                i++;
                
                if (c is System.Windows.Forms.TextBox)
                {
                    try
                    {
                        int count = int.Parse(c.Text);
                        cp2.count = count;
                        cp.count = count;
                        productItem.count = count;
                    } catch (Exception q) { flag = 1; MessageBox.Show("输入类型有误");return; }
                                    
                }
                if (c is System.Windows.Forms.Label)
                {
                    if (c.Name.Equals("cpid"))
                    {
                        cp.id=int.Parse(c.Text);
                        cp2.id = int.Parse(c.Text);
                        productItem.ID = int.Parse(c.Text);
                        //  Console.WriteLine(c.Text + "cpid");
                    }
                    else

                    if (c.Name.Equals("cpname"))
                    {
                        cp.name=c.Text;
                        cp2.name = c.Text;
                        // Console.WriteLine(c.Text + "cpname");
                    }
                    else

                    if (c.Name.Equals("cpprice"))
                    {
                        cp.price = double.Parse(c.Tag.ToString());
                        cp2.price = double.Parse(c.Tag.ToString());
                        //  Console.WriteLine(c.Text + "cpprice");
                    }
                    else

                    if (c.Name.Equals("cpmemberprice"))
                    {
                        cp.memberPrice = double.Parse(c.Tag.ToString());
                        cp2.memberPrice = double.Parse(c.Tag.ToString());
                        // Console.WriteLine(c.Text + "memberprice");
                    }

                    else
                    if (c.Name.Equals("sellStatus"))
                    {
                        if (c.Tag.ToString().Equals("停售"))
                        {
                            flag = 1;
                            MessageBox.Show("停售中");

                        }
                        else if(c.Tag.ToString().Equals("未销售"))
                        {
                            flag = 1;
                            MessageBox.Show("未销售");
                        }

                       // Console.WriteLine(c.Text + "sellStatus");
                    }
                   

                }
                if (c is System.Windows.Forms.GroupBox)
                {
                  
                   
                   
                    foreach (Control cc in c.Controls)
                    {
                        CpAttribution attr = new CpAttribution();
                        ProdAttrItem prodAttr = new ProdAttrItem();
                        if (cc is System.Windows.Forms.RadioButton)
                        {
                            check = true;
                            if (((RadioButton)cc).Checked)
                            {
                                checkFlag.Add(1);
                                attr.id = int.Parse(cc.Name);
                                prodAttr.ID= int.Parse(cc.Name);
                                attr.text = cc.Text;
                                attrname += attr.text + ",";
                                prodAttr.text= cc.Text;
                                attrs.Add(attr);
                              
                                //Console.WriteLine(cc.Name + "被选中");
                            }
                        }
                        cp2.attrs = attrs;
                        cp.attrs = attrs;
                    }
                   
                }
               

            }
            if (check)
            {
                if (checkFlag.Count < 1)
                {
                    flag = 1;
                    MessageBox.Show("至少选择一个属性");
                    return;
                }
            }
           
            if (!attrname.Equals(""))
            {
                attrname = attrname.Substring(0, attrname.LastIndexOf(","));
                cp.name = cp.name + "(" + attrname + ")";
                cp2.name = cp2.name + "(" + attrname + ")";
            }
           
            if (i==0)
            {
                flag = 1;
                MessageBox.Show("请在全部菜品列表中选择");
            }
            if (flag == 0)
            {
                
                if (cp.count==0)
                {
                    cp.count = 1;
                }
                if (cp2.count == 0)
                {
                    cp2.count = 1;
                }
                cp.flag = 5;//代表还未添加到真实订单中
                cp2.flag = 5;
                cp.removeId = System.Guid.NewGuid().ToString("N");
                cp2.removeId = cp.removeId;
                 int iflag = 0;
                 foreach (Cp c in orderCp)
                  {
                     if (c.id == cp.id)
                    {
                        if ( c.removeId != null)
                        {
                              if (cp.attrs != null)
                              {
                                if (c.attrs != null)
                                {
                                    var exp1 = c.attrs.Where(a => cp.attrs.Exists(t => a.text.Contains(t.text))).ToList();
                                    if (exp1.Count > 0 && exp1.Count == c.attrs.Count)
                                    {
                                        iflag = 1;
                                        c.count += cp.count;
                                    }

                                }
                            }
                              else
                              {
                                  iflag = 1;
                                   c.count += cp.count;
                              }

                         }
                    }
                }
                if (iflag!=1)
                {
                orderCp.Add(cp);
                }
                
               
                List<Cp> cpNew = new List<Cp>();
                foreach (Cp c in orderCp)
                {                
                    cpNew.Add(c);
                }
              
                this.dataGridView3.DataSource = cpNew;

                int iflag2 = 0;
             
                foreach (Cp c in addCp)
                {
                    if (c.id == cp2.id)
                    {
                        if (cp2.attrs != null)
                        {
                           
                            if (c.attrs!=null)
                            {
                                var exp1 = c.attrs.Where(a => cp2.attrs.Exists(t => a.text.Contains(t.text))).ToList();
                                if (exp1.Count>0&& exp1.Count==c.attrs.Count)
                                {
                                    iflag2 = 1;
                                    c.count += cp2.count;
                                }
                               
                            }
                        }
                        else
                        {
                            iflag2 = 1;
                            c.count += cp2.count;
                        }

                    }

                }
                if (iflag2 != 1)
                {
                    addCp.Add(cp2);
                }
            }
        }

        private void button6_Click(object sender, EventArgs e)
        {
           try {
            int check = 0;
           
            if (this.dataGridView3.Rows.Count > 0)
            {
                List<Cp> cps = new List<Cp>();
                for (int i = 0; i < dataGridView3.Rows.Count; i++)
                {
                    DataGridViewCheckBoxCell checkCell = (DataGridViewCheckBoxCell)this.dataGridView3.Rows[i].Cells["check1"];
                    Boolean flag = Convert.ToBoolean(checkCell.Value);
                    if (flag)
                    {
                        check++;
                        //未在订单中的菜品
                        if (dataGridView3.Rows[i].Cells["flag"].Value != null&&dataGridView3.Rows[i].Cells["flag"].Value.ToString().Equals("5"))
                        {
                            for (int j = 0; j < addCp.Count; j++)
                            {
                                if (addCp[j].removeId ==dataGridView3.Rows[i].Cells["removeId"].Value.ToString())
                                {
                                        if (int.Parse(dataGridView3.Rows[i].Cells["count"].Value.ToString()) == 0)
                                        {
                                            addCp.RemoveAt(j);
                                        }
                                        else
                                        {
                                            addCp[j].count = int.Parse(dataGridView3.Rows[i].Cells["count"].Value.ToString());
                                        }
                                                                      
                                }
                            }
                            
                            for (int j = 0; j < orderCp.Count; j++)
                            {
                                if (orderCp[j].removeId ==dataGridView3.Rows[i].Cells["removeId"].Value.ToString())
                                {
                                    
                                        if (int.Parse(dataGridView3.Rows[i].Cells["count"].Value.ToString()) == 0)
                                        {
                                            //orderCp.RemoveAt(j);
                                        }
                                        else
                                        {
                                            orderCp[j].count = int.Parse(dataGridView3.Rows[i].Cells["count"].Value.ToString());
                                        }
                                    }          
                              
                            }
                           
                        }
                        else
                        {
                            for (int j = 0; j < orderCp.Count; j++)
                            {
                                if (orderCp[j].ODID == int.Parse(dataGridView3.Rows[i].Cells["ODID"].Value.ToString()))
                                {
                                        bool flag2 = true;
                                        //多次修改菜品数量
                                        foreach (DetailItem de in removeCp)
                                        {
                                            if (de.ODID == int.Parse(dataGridView3.Rows[i].Cells["ODID"].Value.ToString()))
                                            {
                                                flag2 = false;
                                                de.count= int.Parse(dataGridView3.Rows[i].Cells["count"].Value.ToString());
                                            }
                                           
                                        }
                                        if (flag2)
                                        {
                                            DetailItem detail = new DetailItem();
                                            detail.ODID = int.Parse(dataGridView3.Rows[i].Cells["ODID"].Value.ToString());
                                            if (int.Parse(dataGridView3.Rows[i].Cells["count"].Value.ToString()) == 0)
                                            {
                                                detail.count = 0;
                                                // orderCp.RemoveAt(j);
                                            }
                                            else
                                            {
                                                detail.count = int.Parse(dataGridView3.Rows[i].Cells["count"].Value.ToString());
                                            }
                                            removeCp.Add(detail);
                                        }
                                       
                                        break;
                                   
                                }

                            }
                            
                        }
                        
                    }
                }
               
                for (int j = 0; j < orderCp.Count; j++)
                {
                    cps.Add(orderCp[j]);
                }
              
                this.dataGridView3.DataSource = cps;
            }

            if (check==0)
            {
                MessageBox.Show("请在订单列表中选择");
            }
           }
            catch (Exception q) { MessageBox.Show(q.Message); }
        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex>=0)
            {
                this.panel1.Controls.Clear();
                if (this.dataGridView1.Rows[e.RowIndex].Cells["flag"].Value.ToString().Equals("1"))
                {
                    int sizeflag = 0;
                    int tasteflag = 0;
                    List<CpAttribution> cpAttr = (List<CpAttribution>)dataGridView1.Rows[e.RowIndex].Cells["attrs"].Value;

                    GroupBox sizeBox = new GroupBox();
                    sizeBox.Text = "规格";
                    sizeBox.Left = 6;
                    sizeBox.Top = 5;
                    GroupBox tasteBox = new GroupBox();
                    tasteBox.Text = "口味";
                    tasteBox.Left = 6;
                    tasteBox.Top = 10 + sizeBox.Height;

                    Label count = new Label();
                    count.AutoSize = true;
                    count.Size = new Size(20,20);
                    count.Text = "数量";
                    count.Left = 6;
                    count.Top = 14+ count.Height+tasteBox.Top+80;

                    TextBox countText = new TextBox();
                    countText.Text = "1";
                    countText.Left = 6+ count.Width+18;
                    countText.Top = 10 + count.Height+ tasteBox.Top+80;

                    
                    Label cpid = new Label();
                    cpid.Visible = false;
                    cpid.Name = "cpid";
                    cpid.Text = this.dataGridView1.Rows[e.RowIndex].Cells["id"].Value.ToString();
                    cpid.Left = 20;
                    cpid.Top = 10 + tasteBox.Height;

                    Label cpname = new Label();
                    cpname.Name = "cpname";
                    cpname.Text = this.dataGridView1.Rows[e.RowIndex].Cells["name"].Value.ToString();
                    cpname.Left = 6;
                    cpname.Top = 10 + cpname.Height+count.Top;
                    this.panel1.Controls.Add(cpname);

                    Label cpprice = new Label();
                    cpprice.Name = "cpprice";
                    cpprice.Text = "价格:" + this.dataGridView1.Rows[e.RowIndex].Cells["price"].Value.ToString();
                    cpprice.Tag = this.dataGridView1.Rows[e.RowIndex].Cells["price"].Value.ToString();
                    cpprice.Left = 6;
                    cpprice.Top = 10 + cpprice.Height+ cpname.Top;
                    this.panel1.Controls.Add(cpprice);

                    Label cpmemberprice = new Label();
                    cpmemberprice.Name = "cpmemberprice";
                    cpmemberprice.Text = "会员价:"+this.dataGridView1.Rows[e.RowIndex].Cells["memberprice"].Value.ToString();
                    cpmemberprice.Tag = this.dataGridView1.Rows[e.RowIndex].Cells["memberprice"].Value.ToString();
                    cpmemberprice.Left = 6;
                    cpmemberprice.Top = 10 + cpmemberprice.Height+ cpprice.Top;
                    this.panel1.Controls.Add(cpmemberprice);

                    Label sellstate = new Label();
                    sellstate.Name = "sellStatus";
                    sellstate.Text = "状态:" + this.dataGridView1.Rows[e.RowIndex].Cells["state"].Value.ToString();
                    sellstate.Tag = this.dataGridView1.Rows[e.RowIndex].Cells["state"].Value.ToString();
                    sellstate.Left = 6;
                    sellstate.Top = 10 + sellstate.Height+ cpmemberprice.Top;
                    this.panel1.Controls.Add(sellstate);

                    List<CpAttribution> sizeAttr = new List<CpAttribution>();
                    List<CpAttribution> tasteAttr = new List<CpAttribution>();
                    for (int i = 0; i < cpAttr.Count; i++)
                    {
                        if (cpAttr[i].type == 1)
                        {
                            sizeAttr.Add(cpAttr[i]);                         
                        }
                        else
                        {
                            tasteAttr.Add(cpAttr[i]);
                        }
                    }
                    if (sizeAttr.Count>0)
                    {
                        sizeflag = 1;
                        for (int i = 0; i < sizeAttr.Count; i++)
                        {
                            Console.WriteLine("sizeAttr[i].id.ToString();" + sizeAttr[i].id.ToString());
                            RadioButton sizeRa = new RadioButton();
                            sizeRa.AutoSize = true;
                            sizeRa.Name = sizeAttr[i].id.ToString();
                            sizeRa.Size = new Size(20, 20);
                            sizeRa.Text =sizeAttr[i].text;
                            sizeRa.Top = 15;
                            if (i == 0)
                            {
                                sizeRa.Left = 10;
                            }
                            else
                            {
                                sizeRa.Left = 40+sizeRa.Width;
                            }
                            sizeBox.Controls.Add(sizeRa);
                        }
                    }
                    if (tasteAttr.Count > 0)
                    {
                        tasteflag = 1;
                        for (int i = 0; i < tasteAttr.Count; i++)
                        {
                            Console.WriteLine("tasteAttr[i].id.ToString();" + tasteAttr[i].id.ToString());
                            RadioButton tasteRa = new RadioButton();
                            tasteRa.AutoSize = true;
                            tasteRa.Name = tasteAttr[i].id.ToString();
                            tasteRa.Size = new Size(20,20);
                            tasteRa.Text = tasteAttr[i].text;
                            tasteRa.Top = 15;
                            if (i == 0)
                            {
                                tasteRa.Left = 10;
                            }
                            else
                            {
                                tasteRa.Left = 40+tasteRa.Width;
                            }
                            tasteBox.Controls.Add(tasteRa);
                        }
                    }
                   
                    if (sizeflag == 1)
                    {
                        this.panel1.Controls.Add(sizeBox);
                    }
                    if (tasteflag == 1)
                    {
                        this.panel1.Controls.Add(tasteBox);
                    }
                    this.panel1.Controls.Add(count);
                    this.panel1.Controls.Add(cpid);
                    this.panel1.Controls.Add(countText);
                    this.Size = new Size(836, 535);
                   
                }
                else
                {
                    Label cpid = new Label();
                    cpid.Visible = false;
                    cpid.Name = "cpid";
                    cpid.Text = this.dataGridView1.Rows[e.RowIndex].Cells["id"].Value.ToString();
                    cpid.Left = 20;
                    cpid.Top = 10 + cpid.Height;
                    this.panel1.Controls.Add(cpid);
                    Label count = new Label();
                    count.AutoSize = true;
                    count.Size = new Size(20, 20);
                    count.Text = "数量";
                    count.Left = 6;
                    count.Top = 14 + count.Height + cpid.Top;
                    this.panel1.Controls.Add(count);
                    TextBox countText = new TextBox();
                    countText.Text = "1";
                    countText.Left = 6 + count.Width + 18;
                    countText.Top = 16 + count.Height  +cpid.Top;
                    this.panel1.Controls.Add(countText);
                   

                    Label cpname = new Label();
                    cpname.Name = "cpname";
                    cpname.Text = this.dataGridView1.Rows[e.RowIndex].Cells["name"].Value.ToString();
                    cpname.Left = 6;
                    cpname.Top = 10 + cpname.Height + countText.Top;
                    this.panel1.Controls.Add(cpname);

                    Label cpprice = new Label();
                    cpprice.Name = "cpprice";
                    cpprice.Text = "价格:" + this.dataGridView1.Rows[e.RowIndex].Cells["price"].Value.ToString();
                    cpprice.Tag = this.dataGridView1.Rows[e.RowIndex].Cells["price"].Value.ToString();
                    cpprice.Left = 6;
                    cpprice.Top = 10 + cpprice.Height + cpname.Top;
                    this.panel1.Controls.Add(cpprice);

                    Label cpmemberprice = new Label();
                    cpmemberprice.Name = "cpmemberprice";
                    cpmemberprice.Text = "会员价:" + this.dataGridView1.Rows[e.RowIndex].Cells["memberprice"].Value.ToString();
                    cpmemberprice.Tag = this.dataGridView1.Rows[e.RowIndex].Cells["memberprice"].Value.ToString();
                    cpmemberprice.Left = 6;
                    cpmemberprice.Top = 10 + cpmemberprice.Height + cpprice.Top;
                    this.panel1.Controls.Add(cpmemberprice);

                    Label sellstate = new Label();
                    sellstate.Name = "sellStatus";
                    sellstate.Text = "状态:" + this.dataGridView1.Rows[e.RowIndex].Cells["state"].Value.ToString();
                    sellstate.Tag = this.dataGridView1.Rows[e.RowIndex].Cells["state"].Value.ToString();
                    sellstate.Left = 6;
                    sellstate.Top = 10 + sellstate.Height + cpmemberprice.Top;
                    this.panel1.Controls.Add(sellstate);
                    this.Size = new Size(836, 535);
                }
              
            }
          
        }

        private void button7_Click(object sender, EventArgs e)
        {
            List<ProductItem> productItemCp = new List<ProductItem>();
             try
              {
            foreach (Cp cp in addCp)
            {
                ProductItem pro = new ProductItem();
                pro.ID = cp.id;
                pro.count = cp.count;
               
                if (cp.attrs != null)
                {
                    List<ProdAttrItem> productAttr = new List<ProdAttrItem>();
                   
                    foreach (CpAttribution attr in cp.attrs)
                    {
                        ProdAttrItem proattr = new ProdAttrItem();
                        proattr.ID = attr.id;
                        proattr.text = attr.text;
                        productAttr.Add(proattr);
                    }
                    pro.attrs = productAttr;
                }
                productItemCp.Add(pro);
            }

                string jsonData= JsonConvert.SerializeObject(productItemCp);
                string removes=JsonConvert.SerializeObject(removeCp);
                JArray attached = JArray.Parse(jsonData);
                JArray removesCp = JArray.Parse(removes);
                Console.WriteLine("orderNo" + orderNo);

                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 92 }, { "muuid", Login.userInfo.muuid } };
                JObject json3 = new JObject { { "attached", attached }, { "ID", orderId }, { "updates", removesCp } };
                
                json2.Add("data", json3.ToString());
                Console.WriteLine(json2.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());
                if (result != string.Empty)
                {
                    MessageBox.Show("操作成功");
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
            }
          catch (Exception q) { MessageBox.Show(q.Message); }
       }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                this.Size = new Size(624, 535);
                JObject json2 = new JObject { { "seq", 0 }, { "ver", 0 }, { "op", 2 }, { "muuid", Login.userInfo.muuid } };
                JObject json3 = new JObject { { "search", this.cpname.Text } };

                json2.Add("data", json3.ToString());
                string result = string.Empty;
                result = MainForm.HttpPostData(MainForm.testurl.ToString(), json2.ToString());

                getCps(result);

            }
            catch (Exception q) { MessageBox.Show(q.Message); }

        }
        public void getCps(string result)
        {
            if (result != string.Empty)
            {
                Console.WriteLine("cps" + result);
                JObject jo = (JObject)JsonConvert.DeserializeObject(result);
                if (jo["errorCode"].ToString().Equals("0"))
                {
                    JObject jsonObect = (JObject)JsonConvert.DeserializeObject(jo["data"].ToString());
                    if (int.Parse(jsonObect["count"].ToString()) > 0)
                    {
                        JArray orderCpArray = JArray.Parse(jsonObect["items"].ToString());
                        List<Cp> cps = new List<Cp>();
                        for (int j = 0; j < orderCpArray.Count; j++)
                        {
                            Cp cp = new Cp();
                            cp.id = int.Parse(orderCpArray[j]["ID"].ToString());

                            cp.name = orderCpArray[j]["name"].ToString();

                            if (int.Parse(orderCpArray[j]["sellStatus"].ToString()) == 0)
                            {
                                cp.sellStatus = "未销售";
                            }
                            else if (int.Parse(orderCpArray[j]["sellStatus"].ToString()) == -1)
                            {
                                cp.sellStatus = "停售";
                            }
                            else if (int.Parse(orderCpArray[j]["sellStatus"].ToString()) == 1)
                            {
                                cp.sellStatus = "销售中";
                            }
                            cp.memberPrice = double.Parse(orderCpArray[j]["vipPrice"].ToString());
                            cp.price = double.Parse(orderCpArray[j]["price"].ToString());
                            cp.flag = 0;
                            if (jsonObect["productAttrs"] != null)
                            {
                                JArray attrArray = JArray.Parse(jsonObect["productAttrs"].ToString());
                                List<CpAttribution> attrs = new List<CpAttribution>();
                                for (int i = 0; i < attrArray.Count; i++)
                                {
                                    if (cp.id == int.Parse(attrArray[i]["productID"].ToString()))
                                    {
                                        CpAttribution cpattr = new CpAttribution();
                                        cpattr.id = int.Parse(attrArray[i]["ID"].ToString());
                                        cpattr.type = int.Parse(attrArray[i]["type"].ToString());
                                        cpattr.price = double.Parse(attrArray[i]["price"].ToString());
                                        cpattr.cpid = int.Parse(attrArray[i]["productID"].ToString());
                                        cpattr.text = attrArray[i]["name"].ToString();

                                        attrs.Add(cpattr);
                                        cp.flag = 1;
                                    }
                                }
                                cp.attrs = attrs;

                            }

                            cps.Add(cp);

                        }
                        this.dataGridView1.DataSource = cps;
                    }
                    else
                    {
                        MessageBox.Show("暂无数据");
                    }

                }

            }
        }

        private void dataGridView3_CellValidating(object sender, DataGridViewCellValidatingEventArgs e)
        {
            if (e.RowIndex > -1 && e.ColumnIndex > -1)
           {
               DataGridView grid = (DataGridView)sender;
               grid.Rows[e.RowIndex].ErrorText = "";
               //这里最好用列名，而不是列索引号做判断
               if (grid.Columns[e.ColumnIndex].Name == "count" )
               {
                  Int32 newInteger = 0;
                    
                  if (!int.TryParse(e.FormattedValue.ToString(), out newInteger))
                  {
                     e.Cancel = true;
                     grid.Rows[e.RowIndex].ErrorText = "请输入正整数";
                     MessageBox.Show("请输入正整数");
                     return;
                  }else
                    if (double.Parse(e.FormattedValue.ToString()) < 0)
                    {
                        e.Cancel = true;
                        grid.Rows[e.RowIndex].ErrorText = "请输入正整数";
                        MessageBox.Show("请输入正整数");
                        return;
                    }
                }
            }
        }
    }
}
