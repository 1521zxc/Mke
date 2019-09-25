//#define WIN64

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime;
using System.Runtime.InteropServices;

namespace DeviceDecode
{
    class PPDriver
    {
#if WIN64
    public const string g_strDriverName = "64RC532.dll";
#else
    public const string g_strDriverName = "dll_camera.dll";
#endif

        [DllImport(g_strDriverName, EntryPoint = "GetDevice", CallingConvention = CallingConvention.Cdecl)]
        public static extern int GetDevice();

        [DllImport(g_strDriverName, EntryPoint = "StartDevice", CallingConvention = CallingConvention.Cdecl)]
        public static extern int StartDevice();

        [DllImport(g_strDriverName, EntryPoint = "GetAppHandle", CallingConvention = CallingConvention.Cdecl)]
        public static extern void GetAppHandle(IntPtr hWnd);

        [DllImport(g_strDriverName, EntryPoint = "ReleaseDevice", CallingConvention = CallingConvention.Cdecl)]
        public static extern void ReleaseDevice();

        [DllImport(g_strDriverName, EntryPoint = "ReleaseLostDevice", CallingConvention = CallingConvention.Cdecl)]
        public static extern void ReleaseLostDevice();

        [DllImport(g_strDriverName, EntryPoint = "setQRable", CallingConvention = CallingConvention.Cdecl)]
        public static extern void setQRable(bool bQR);

        [DllImport(g_strDriverName, EntryPoint = "setBarcode", CallingConvention = CallingConvention.Cdecl)]
        public static extern void setBarcode(bool bBarcode);

        [DllImport(g_strDriverName, EntryPoint = "setDMable", CallingConvention = CallingConvention.Cdecl)]
        public static extern void setDMable(bool bDM);

        [DllImport(g_strDriverName, EntryPoint = "setHXable", CallingConvention = CallingConvention.Cdecl)]
        public static extern void setHXable(bool bHX);

        [DllImport(g_strDriverName, EntryPoint = "SetBeep", CallingConvention = CallingConvention.Cdecl)]
        public static extern void SetBeep(bool bEnableBeep);

        [DllImport(g_strDriverName, EntryPoint = "SetBeepTime", CallingConvention = CallingConvention.Cdecl)]
        public static extern void SetBeepTime(int nBeepTime);

        [DllImport(g_strDriverName, EntryPoint = "SetLED", CallingConvention = CallingConvention.Cdecl)]
        public static extern void SetLed(int nState);

        [DllImport(g_strDriverName, EntryPoint = "GetDecodeString", CallingConvention = CallingConvention.Cdecl)]
        public static extern int GetDecodeString(StringBuilder sb, out int len);
    }
}
