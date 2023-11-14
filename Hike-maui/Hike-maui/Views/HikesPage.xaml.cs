using System;
using System.Linq;
using Hike_maui;
using Hikemaui.Extensions;
using Hikemaui.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace Hikemaui.Views
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class HikesPage : ContentPage
	{
		private SQLiteHelper _dbHelper;

		public HikesPage()
		{
			InitializeComponent();
			_dbHelper = App.DatabaseHelper;
		}

		protected override async void OnAppearing()
		{
			try
			{
				base.OnAppearing();
				_dbHelper = App.DatabaseHelper;

				hikeCollectionView.ItemsSource = await _dbHelper.GetAllHikeAsync();
			}
			catch (Exception)
			{
				throw;
			}
		}

		private async void ToolbarItem_Clicked(object sender, EventArgs e)
		{
			await Navigation.PushAsync(new HikeAddAndUpdate());
        }

		private async void SearchBar_TextChanged(object sender, TextChangedEventArgs e)
		{
			hikeCollectionView.ItemsSource = await _dbHelper.SearchAsync(e.NewTextValue);
		}

		private void HikeCollectionView_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			if (e.CurrentSelection.FirstOrDefault() is HikeModel selectedHike)
			{
				hikeCollectionView.SelectedItem = null;
				Navigation.PushAsync(new HikeDetails(selectedHike));
			}
		}

		private async void SwipeItem_InvokedAsync_Delete(object sender, EventArgs e)
		{
			var item = sender as SwipeItem;
			var hikeModel = item.CommandParameter as HikeModel;

			var result = await DisplayAlert("Delete", $"Are you sure that you want to delete {hikeModel.Name}?", "Yes", "No");

			if(result)
			{
				await _dbHelper.DeleteHikeAsync(hikeModel);
				hikeCollectionView.ItemsSource = await _dbHelper.GetAllHikeAsync();
			}
		}

		private async void SwipeItem_InvokedAsync_Edit(object sender, EventArgs e)
		{
			var item = sender as SwipeItem;
			var hikeModel = item.CommandParameter as HikeModel;

			await Navigation.PushAsync(new HikeAddAndUpdate(hikeModel));
		}
	}
}