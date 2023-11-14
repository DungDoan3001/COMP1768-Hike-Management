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
	public partial class HikeDetails : ContentPage
	{
		private readonly HikeModel _hikeModel;
		private SQLiteHelper _dbHelper = App.DatabaseHelper;


		public HikeDetails(HikeModel hikeModel)
		{
			InitializeComponent();
			BindingContext = hikeModel;
			_hikeModel = hikeModel;
		}

		protected override async void OnAppearing()
		{
			base.OnAppearing();
			ObservationCollectionView.ItemsSource = await _dbHelper.GetObservationsByHikeIdAsync(_hikeModel.Id);
		}

		private async void Button_Clicked(object sender, EventArgs e)
		{
			await Navigation.PushAsync(new ObservationAddAndUpdate(_hikeModel));
		}

		private async void ObservationCollectionView_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			if (e.CurrentSelection.FirstOrDefault() is ObservationModel selectedObservation)
			{
				ObservationCollectionView.SelectedItem = null;
				await Navigation.PushAsync(new ObservationDetails(selectedObservation));
			}
		}

		private async void SwipeItemObservation_InvokeAsync_Delete(object sender, EventArgs e)
		{
			var item = sender as SwipeItem;
			var observationModel = item.CommandParameter as ObservationModel;

			var result = await DisplayAlert("Delete", $"Are you sure to delete {observationModel.Name}?", "Yes", "No");

			if (result)
			{
				await _dbHelper.DeleteObservationAsync(observationModel);
				ObservationCollectionView.ItemsSource = await _dbHelper.GetObservationsByHikeIdAsync(_hikeModel.Id);
			}
		}

		private async void SwipeItemObservation_InvokeAsync_Edit(object sender, EventArgs e)
		{
			var item = sender as SwipeItem;
			var observationModel = item.CommandParameter as ObservationModel;

			await Navigation.PushAsync(new ObservationAddAndUpdate(observationModel));
		}
	}
}