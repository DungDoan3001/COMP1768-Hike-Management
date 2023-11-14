using System;
using Hike_maui;
using Hikemaui.Extensions;
using Hikemaui.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace Hikemaui.Views
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class HikeAddAndUpdate : ContentPage
	{
		private readonly HikeModel _hikeModel;
		private SQLiteHelper _dbHelper;


		public HikeAddAndUpdate()
		{
			InitializeComponent();
			_dbHelper = App.DatabaseHelper;
		}

		public HikeAddAndUpdate(HikeModel hikeModel)
		{
			InitializeComponent();
			
			Title = "Edit hike";
			_dbHelper = App.DatabaseHelper;
			_hikeModel = hikeModel;

			nameEntry.Text = _hikeModel.Name;
			datePicker.Date = DateTime.Parse(_hikeModel.Date);
			locationEntry.Text = _hikeModel.Location;
			lengthEntry.Text = _hikeModel.Length;
			parkingEntry.Text = _hikeModel.Parking;
			levelEntry.Text = _hikeModel.Level;
			descriptionEntry.Text = _hikeModel.Description;

			nameEntry.Focus();
		}

		private async void Button_Clicked(object sender, EventArgs e)
		{
			if(string.IsNullOrEmpty(nameEntry.Text))
			{
				await DisplayAlert("Ivalid", "Blank or WhiteSpace value is Invalid", "OK");
			} 
			else if(_hikeModel != null) 
			{
				EditHike();
			} 
			else
			{
				AddNewHike();
			}
        }

		private async void EditHike()
		{
			_hikeModel.Name = nameEntry.Text;
			_hikeModel.Date = datePicker.Date.ToShortDateString();
			_hikeModel.Length = lengthEntry.Text;
			_hikeModel.Location = locationEntry.Text;
			_hikeModel.Description = descriptionEntry.Text;
			_hikeModel.Level = levelEntry.Text;
			_hikeModel.Parking = parkingEntry.Text;

			await _dbHelper.UpdateHikeAsync(_hikeModel);
			await Navigation.PopAsync();
		}

		private async void AddNewHike()
		{
			DateTime date = datePicker.Date;

			await _dbHelper.CreateHikeAsync(new HikeModel
			{
				Name = nameEntry.Text,
				Date = date.ToShortDateString(),
				Location = locationEntry.Text,
				Length = lengthEntry.Text,
				Level = levelEntry.Text,
				Parking = parkingEntry.Text,
				Description = descriptionEntry.Text,
				CreatedAt = DateTime.Now.ToString("hh:mm"),
				UpdatedAt = DateTime.Now.ToString("hh:mm"),
			});

			await Navigation.PopAsync();
		}
    }
}