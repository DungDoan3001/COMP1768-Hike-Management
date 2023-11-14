using System;
using System.IO;
using Hikemaui;
using Hikemaui.Extensions;
using Xamarin.Forms;

namespace Hike_maui
{
	public partial class App : Application
	{
		private static SQLiteHelper _databaseHelper;

		public static SQLiteHelper DatabaseHelper
		{
			get
			{
				if( _databaseHelper == null )
				{
					_databaseHelper = new SQLiteHelper(Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "HIKE_MANAGEMENT.db3"));
				}

				return _databaseHelper;
			}
		}

		public App()
		{
			InitializeComponent();

			MainPage = new MainPage();
		}

		protected override void OnStart()
		{
		}

		protected override void OnSleep()
		{
		}

		protected override void OnResume()
		{
		}
	}
}
