<?php

use App\Models\KatastralneUzemie;
use Illuminate\Database\Seeder;

class KatastralneUzemieSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $katastralne_uzemia = [
            [
                'nazov' => 'Nepriradene',
            ],
            [
                'nazov' => 'Trnava',
            ],
            [
                'nazov' => 'Senec',
            ],
            [
                'nazov' => 'Galanta',
            ]
        ];

        foreach ($katastralne_uzemia as $katastralne_uzemie) {
            KatastralneUzemie::create(array(
                'nazov' => $katastralne_uzemie['nazov']
            ));
        }
    }
}
