<?php

use App\Models\Obec;
use Illuminate\Database\Seeder;

class ObecSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $obce = [
            [
                'nazov' => 'Nepriradena',
            ],
            [
                'nazov' => 'Trnava',
            ],
            [
                'nazov' => 'Senec',
            ],
            [
                'nazov' => 'Galanta',
            ],
            [
                'nazov' => 'Spacince'
            ],
            [
                'nazov' => 'Horne Dubove'
            ]
        ];

        foreach ($obce as $obec) {
            Obec::create(array(
                'nazov' => $obec['nazov']
            ));
        }
    }
}
