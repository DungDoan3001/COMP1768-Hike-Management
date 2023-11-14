using System;
using Xamarin.Forms;

namespace Hikemaui
{
    public partial class MainPage : FlyoutPage
    {
        public MainPage()
        {
            InitializeComponent();
            flyout.listView.ItemSelected += OnSelectedItem;
        }

        private void OnSelectedItem(object sender, SelectedItemChangedEventArgs e)
        {
			if (e.SelectedItem is FlyoutItemPage item)
			{
				Detail = new NavigationPage((Page)Activator.CreateInstance(item.TargetPage));
				IsPresented = false;
			}
		}
    }
}
