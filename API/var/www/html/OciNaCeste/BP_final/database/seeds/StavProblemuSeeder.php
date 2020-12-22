<?php

use App\Models\StavProblemu;
use Illuminate\Database\Seeder;

class StavProblemuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $stavy = [
            [
                'nazov' => 'Chýbajúca',
            ],
            [
                'nazov' => 'Poškodená',
            ],
            [
                'nazov' => 'Vyblednutá',
            ],
            [
                'nazov' => 'Zle viditeľná',
            ],
            [
                'nazov' => 'Prerastená'
            ]
        ];

        foreach ($stavy as $stavy_problemu) {
            StavProblemu::create(array(
                'nazov' => $stavy_problemu['nazov']
            ));
        }
    }
}
